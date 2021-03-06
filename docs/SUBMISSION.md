# CWA-Server Submission Service

## Spring Profiles

Spring profiles are used to apply submission service configuration based on the running environment, determined by the active profile.

You will find `.yaml` and `.xml` based profile-specific configuration files at [`/services/submission/src/main/resources`](/services/submission/src/main/resources).

### Available Profiles

Profile                                           | Effect
--------------------------------------------------|-------------
`dev`                                             | Sets the log level to `DEBUG` and changes the `CONSOLE_LOG_PATTERN` used by Log4j 2.
`cloud`                                           | Removes default values for the `spring.flyway`, `spring.datasource` and `services.submission.verification.base-url` configurations.
`disable-ssl-server`                              | Disables SSL for the submission endpoint.
`disable-ssl-client-postgres`                     | Disables SSL with a pinned certificate for the connection to the postgres.
`disable-ssl-client-verification`                 | Disables SSL with a pinned certificate for the connection to the verification server.
`disable-ssl-client-verification-verify-hostname` | Disables the verification of the SSL hostname for the connection to the verification server.

Please refer to the inline comments in the base `application.yaml` configuration file for further details on the configuration properties impacted by the above profiles.

## TAN Verification

When submitting diagnosis keys, a Transaction Authorization Number (TAN) token must be present in the request header section (`cwa-authorization`).
Before delegating the TAN validation to the Verification Server, the TAN is verified to be a UUID on the Submission Service side.
Then the TAN token is sent to the [Verification Server](https://github.com/corona-warn-app/cwa-verification-server/blob/master/docs/architecture-overview.md)
to check its validity. If the TAN is valid, then it means it is linked to a valid test.
In case the TAN is not valid, then the verification server will respond with `HTTP 404 Not Found` and the Submission Service will respond with `HTTP 403 Forbidden`.

Implementation details can be found in [`TanVerifier.java`](/services/submission/src/main/java/app/coronawarn/server/services/submission/verification/TanVerifier.java).

## Submission Validations

### Custom Annotation [`@ValidSubmissionPayload`](https://corona-warn-app.github.io/cwa-server/1.0.0/app/coronawarn/server/services/submission/validation/ValidSubmissionPayload.html)

You will find the implementation file at [`/services/submission/src/main/java/app/coronawarn/server/services/submission/validation/ValidSubmissionPayload.java`](/services/submission/src/main/java/app/coronawarn/server/services/submission/validation/ValidSubmissionPayload.java)

### Validation Constraints

Temporary Exposure Keys (TEK's) are submitted by the client device (iOS/Android phone) via the Submission Service.

Constraints maintained as enviroment variables which are present as secrets in the Vault /cwa-server/submission

The constraints put on submitted TEK's are as follows:

* Each TEK contains a `StartIntervalNumber` (a date e.g. 2nd July 2020)
* The period covered by the data file must not exceed the configured maximum number of days, represented by the `MAX_NUMBER_OF_KEYS` property which is in the vault.
* The total combined rolling period for a single TEK cannot exceed maximum rolling period, represented by the `MAX_ROLLING_PERIOD` property which is in the vault.
* More than one TEK with the same `StartIntervalNumber` may be submitted, these will have their rolling period's combined.
