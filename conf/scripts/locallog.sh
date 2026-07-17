CI_COMMIT_SHA="0339066b5f274a0d480176152685cab091c4881d"
LAST_SUCCESS="6aab9366c11268a8005db7045b322457d2159c5b"
HASH_LEN=8
CI_COMMIT_BRANCH=prod-v5

changelog="changelog.md"
tmp1=$(mktemp)
tmp2=$(mktemp)

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
