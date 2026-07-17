#!/bin/bash

DATE=$(date +"[%F %H:%M:%S]")

HEADER="🚀 *Build Started* [$DATE]
*📋 Env:* \`${ENV}\`
*🌿 Branch:* \`${CI_COMMIT_BRANCH}\`
*🔖 Revision:* \`${CI_COMMIT_SHORT_SHA}\`
*🔗 Pipeline:* [#${CI_PIPELINE_ID}](${CI_PIPELINE_URL})
*👤 Triggered:* \`${GITLAB_USER_NAME}\`

=== CHANGELOG ($(wc -l changelog.md | awk '{print $1}') commits) ==="

if [ -f changelog.md ]; then
  CHANGELOG=$(cat changelog.md | sed 's/\$/\\\$/g' | tr -d '&')
  MESSAGE="$HEADER"$'\n'"$CHANGELOG"
else
  MESSAGE="$HEADER"$'\n'"No changes found"
fi

# Check combined message size
MESSAGE_SIZE=${#MESSAGE}

if [ "$MESSAGE_SIZE" -lt 4096 ]; then
  RESPONSE=$(curl -s -X POST https://api.telegram.org/bot${TELEGRAM_BOT_TOKEN}/sendMessage \
    -d text="$MESSAGE" \
    -d chat_id=${TELEGRAM_CHAT_ID} \
    -d message_thread_id=${TELEGRAM_THREAD_ID} \
    -d parse_mode='Markdown')
else
  RESPONSE=$(curl -s -X POST https://api.telegram.org/bot${TELEGRAM_BOT_TOKEN}/sendMessage \
    -d text="$HEADER" \
    -d chat_id=${TELEGRAM_CHAT_ID} \
    -d message_thread_id=${TELEGRAM_THREAD_ID} \
    -d parse_mode='Markdown')

  sleep 1

  curl -s -X POST https://api.telegram.org/bot${TELEGRAM_BOT_TOKEN}/sendDocument \
    -F chat_id=${TELEGRAM_CHAT_ID} \
    -F message_thread_id=${TELEGRAM_THREAD_ID} \
    -F document=@changelog.md
fi
MESSAGE_ID=$(echo "$RESPONSE" | jq -r '.result.message_id')
echo "TELEGRAM_MESSAGE_ID=$MESSAGE_ID" > telegram_msg.env
echo "Telegram message ID: $MESSAGE_ID"