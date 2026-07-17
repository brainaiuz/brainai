#!/bin/bash

tmp1=$(mktemp)
tmp2=$(mktemp)
changelog="changelog.md"
HASH_LEN=8

echo "Finding last successful deployment..."

LAST_SUCCESS=$(curl --silent --header "PRIVATE-TOKEN: ${GITLAB_API_TOKEN}" \
  "${CI_API_V4_URL}/projects/${CI_PROJECT_ID}/pipelines?ref=${CI_COMMIT_BRANCH}&status=success" \
  | jq -r '.[0].sha // empty')

if [ -z "$LAST_SUCCESS" ]; then
  echo "No previous successful deployment found"
  LAST_SUCCESS=$(git rev-list --max-parents=0 HEAD)
fi

echo "=== CHANGELOG ==="
echo "Last successful deploy: $LAST_SUCCESS"
echo "Current commit: $CI_COMMIT_SHA"
echo ""

git log origin/"${CI_COMMIT_BRANCH}" --pretty=format:"%H@@@-- \`%s\` _%an_ (\`%h\`)" > "$tmp1"

awk '
  BEGIN {
    FS="@@@"
  } /'"${CI_COMMIT_SHA:0:$HASH_LEN}"'/,/'"${LAST_SUCCESS:0:$HASH_LEN}"'/ {
    if (!/'"${LAST_SUCCESS:0:$HASH_LEN}"'/ && $2 !~ /^-- `Merge /)
      print $2
  }' "$tmp1" > "$tmp2"

awk -F' ' '{
    hash=substr($NF, 3, '$HASH_LEN')
    $NF="(`" hash "`)"
    print
  }' "$tmp2" > "$tmp1"

cat "$tmp1" > "$changelog"

rm "$tmp1" "$tmp2"

COMMIT_COUNT=$(git rev-list --count --no-merges "${LAST_SUCCESS}".."${CI_COMMIT_SHA}")
FILES_CHANGED=$(git diff --name-only "${LAST_SUCCESS}".."${CI_COMMIT_SHA}" | wc -l)

echo ""
echo "=== SUMMARY ==="
echo "Commits: $COMMIT_COUNT"
echo "Files changed: $FILES_CHANGED"

cat > deploy-stats.env <<EOF
COMMIT_COUNT=$COMMIT_COUNT
FILES_CHANGED=$FILES_CHANGED
EOF