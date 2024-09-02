# Frontend - Android, Kotlin

To be able for the user to reach the backend server from the mobile phone, he has to follow a few steps:
1. Connect the mobile to the same WiFi with the computer on which the server is running

2. Find the WiFi's IPv4 IP and put it as a parameter inside `local.properties`.

3. Also, put the port as a parameter inside `local.properties`.

4. Change /res/xml/network_security_config.xml as so:
    ```xml
    <?xml version="1.0" encoding="utf-8"?>
    <network-security-config>
        <domain-config>
            <domain includeSubdomains="true">https://<ip>:<port></domain>
            <trust-anchors>
                <certificates src="@raw/certificate" />
            </trust-anchors>
        </domain-config>
    </network-security-config>
    ```
   ___
   where \<ip> and \<port> are the parameters from the previous steps. 

5. Build the application and you are ready to go.