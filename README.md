
# Schedule API's Service

## /api/v1/schedule
Schedule a specific URL to be fire on a given date and time based on a Cron expression.

| HTTP CODE  | Description |
| ------------- | ------------- |
| 202: Accepted  | The person was successfully deleted   |
| 400: Bad Request  | A Cron Expression might not be a valid value |
| 409: Conflict  | Indicate a Schedule with a given name already exists |


