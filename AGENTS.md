# Agent Instructions

This file contains instructions for the software engineering agent.

## Publishing the `liveTrackingSdk` library

To publish the `liveTrackingSdk` library to the Sonatype OSSRH (Maven Central), you need to provide your credentials in the `local.properties` file in the root directory of the project.

Create a `local.properties` file in the root directory of the project if it doesn't exist, and add the following lines:

```
sonatypeUsername=AdhamAlkhawalda
sonatypePassword=Ab123456789$
```

Replace `<your-sonatype-username>` and `<your-sonatype-password>` with your actual Sonatype username and password.

**Do not commit this file to version control.** The `local.properties` file is already in the `.gitignore` file, so it won't be committed by default.
