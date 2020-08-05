<script type="application/javascript">
    function refreshCallbackJson(){
        document.getElementById('callback_json').innerText='{\n    "activationId": "$ACTIVATION_ID"';
        if (document.getElementById('attr_userId').checked) {
            document.getElementById('callback_json').innerText+=',\n    "userId": "$USER_ID"';
        }
        if (document.getElementById('attr_activationName').checked) {
            document.getElementById('callback_json').innerText+=',\n    "activationName": "$ACTIVATION_NAME"';
        }
        if (document.getElementById('attr_deviceInfo').checked) {
            document.getElementById('callback_json').innerText+=',\n    "deviceInfo": "$DEVICE_INFO"';
        }
        if (document.getElementById('attr_platform').checked) {
            document.getElementById('callback_json').innerText+=',\n    "platform": "$PLATFORM"';
        }
        if (document.getElementById('attr_activationFlags').checked) {
            document.getElementById('callback_json').innerText+=',\n    "activationFlags": "$ACTIVATION_FLAGS"';
        }
        if (document.getElementById('attr_activationStatus').checked) {
            document.getElementById('callback_json').innerText+=',\n    "activationStatus": "$ACTIVATION_STATUS"';
        }
        if (document.getElementById('attr_blockedReason').checked) {
            document.getElementById('callback_json').innerText+=',\n    "blockedReason": "$BLOCKED_REASON"';
        }
        if (document.getElementById('attr_applicationId').checked) {
            document.getElementById('callback_json').innerText+=',\n    "applicationId": "$APPLICATION_ID"';
        }
        document.getElementById('callback_json').innerText+='\n}';
    }
    refreshCallbackJson();
</script>