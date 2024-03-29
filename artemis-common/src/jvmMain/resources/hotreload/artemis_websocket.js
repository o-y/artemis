// TODO:
//  I'd like to write this in Kotlin one day...
//  This is possible if I refactor everything other than the plugin into artemis-common
//  then I have this placed in jsMain with a run immediately export (somehow) and have
//  this built as part of the publish step, with an additional task to copy this into
//  the jar resources file before it's published, then just point to that.

const SERVER_HASH_ACK = "artemis-ws:server:hash-ack"
const CLIENT_PING = "artemis-ws:client:ping"

const host = window.location.hostname;
const port = window.location.port;

let deploymentHash = ""

const connect = () => {
    try {
        const socket = new WebSocket('ws://' + host + ':' + port);

        socket.onopen = () => {
            socket.send(CLIENT_PING)
        };

        socket.onmessage = (event) => {
            if (event.data.startsWith(SERVER_HASH_ACK)) {
                let formattedHash = event.data.substring(event.data.lastIndexOf(":") + 1);

                if (deploymentHash !== formattedHash && deploymentHash !== "") {
                    console.log("Deployment hash changed! Previous:", deploymentHash, "New:", formattedHash);
                    location.reload()
                } else {
                    console.log("Deployment hash remains the same:", formattedHash);
                }
                deploymentHash = formattedHash;
            }
        };

        socket.onclose = () => {
            setTimeout(connect, 100);
        };

        socket.onerror = () => {
            socket.close();
        };
    } catch (e) {}
};

connect();