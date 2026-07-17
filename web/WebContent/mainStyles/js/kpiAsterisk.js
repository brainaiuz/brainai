var simpleUsers = [];
var asteriskHost;
var ringAudio = new Audio('/mainStyles/js/ringbacktone.wav');
var onAsteriskConnected, onAsteriskDisconnected, onAsteriskRegistered, onAsteriskUnregistered, onAsteriskCallReceived,
    onAsteriskCallHangup,
    onAsteriskCallAnswered, onAsteriskCallHold;

function setupAsterisk(username, password, displayName, host, wsport, onConnected, onDisconnected, onRegistered, onUnregistered,
                       onCallReceived, onCallAnswered, onCallHangup, onCallHold) {

    this.asteriskHost = host;
    ringAudio.loop = true;
    ringAudio.volume = 1;
    onAsteriskCallReceived = onCallReceived;
    onAsteriskCallHangup = onCallHangup;
    onAsteriskConnected = onConnected;
    onAsteriskDisconnected = onDisconnected;
    onAsteriskRegistered = onRegistered;
    onAsteriskUnregistered = onUnregistered;
    onAsteriskCallAnswered = onCallAnswered;
    onAsteriskCallHold = onCallHold;

    //Set default port if not specified
    if (!wsport) {
        wsport = 18089;
    }

    //here you determine whether the call has video and audio
    var options = {
        media: {
            local: {
                video: document.getElementById('localVideo'),
                audio: document.getElementById('localVideo')
            },
            remote: {
                video: document.getElementById('remoteVideo'),
                // This is necessary to do an audio/video call as opposed to just a video call
                audio: document.getElementById('remoteVideo')
            }
        },
        aor: 'sip:' + username + '@' + host,
        userAgentOptions: {
            displayName: displayName,
            authorizationUsername: username,
            authorizationPassword: password,
            iceCheckingTimeout: 10
            // uri: 'sip:101@10.200.2.50'
        },
        delegate: {
            onServerConnect: function () {
                console.log('Asterisk is connected !');
                if (onAsteriskConnected) {
                    onAsteriskConnected(username);
                }
            },
            onServerDisconnect: function () {
                console.log('Asterisk is disconnected !');
                if (onAsteriskDisconnected) {
                    onAsteriskDisconnected(username);
                }
                ringAudio.pause();
            },
            onCallReceived: function (invite) {
                console.log('Asterisk call received !');
                // Initiate ring sound
                ringAudio.play();

                if (onAsteriskCallReceived) {
                    onAsteriskCallReceived(username, invite.remoteIdentity.uri.raw.user);
                } else {
                    console.log('onCallREceived is null')
                }

            },
            onCallHangup: function () {
                console.log('Asterisk call hangup ! ' + username);
                ringAudio.pause();
                if (onAsteriskCallHangup) {
                    onAsteriskCallHangup(username);
                }
                if (onConnected && !simpleUsers[username].isConnected()) {
                    onConnected(username);
                }
            },
            onCallAnswered: function () {
                console.log('Asterisk call answered !');
                ringAudio.pause();
                if (onAsteriskCallAnswered) {
                    onAsteriskCallAnswered(username);
                }
            },
            onCallHold: function () {
                console.log('Asterisk call hold !');
                ringAudio.pause();
                if (onAsteriskCallHold) {
                    onAsteriskCallHold(username);
                }
            }, onCallDTMFReceived: function (tone, duration) {
                console.log('Asterisk DTMF received ! duration: ' + duration + '  - tone: ' + tone);

            }, /*onSubscribeRequest: function () {
                console.log('onSubscribeRequest  !!!!!!!!!!!!!!!!!!');

            }, onReferRequest: function () {
                console.log('onReferRequest  !!!!!!!!!!!!!!!!!!');

            }, onNotify: function () {
                console.log('onNotify  !!!!!!!!!!!!!!!!!!');

            }, onInvite: function () {
                console.log('onInvite  !!!!!!!!!!!!!!!!!!');

            }, onRinging: function () {
                console.log('onRinging  !!!!!!!!!!!!!!!!!!');

            }, onMessage: function () {
                console.log('onMessage  !!!!!!!!!!!!!!!!!!');

            },*/ onMessageReceived: function () {
                console.log('Asterisk Message received !');

            }, onRegistered: function () {
                console.log('Asterisk User Agent registered !');
                if (onAsteriskRegistered) {
                    onAsteriskRegistered(username);
                }
            }, onUnregistered: function () {
                console.log('onUnregistered !!!!!!!!!!!!!!!!!!');
                if (onAsteriskUnregistered) {
                    onAsteriskUnregistered(username);
                }
            }, onCallTerminated: function () {
                console.log('onCallTerminated !!!!!!!!!!!!!!!!!!');
                ringAudio.pause();
                if (onAsteriskCallHangup) {
                    onAsteriskCallHangup(username);
                }
            }/*, onReject: function () {
                console.log('onReject !!!!!!!!!!!!!!!!!!');

            }, onProgress: function () {
                console.log('onProgress !!!!!!!!!!!!!!!!!!');

            }, onTrying: function () {
                console.log('onTrying !!!!!!!!!!!!!!!!!!');

            }, onRedirect: function () {
                console.log('onRedirect !!!!!!!!!!!!!!!!!!');

            }, onAccept: function () {
                console.log('onAccept !!!!!!!!!!!!!!!!!!');

            },*/
        }
    };
    console.log('wss://' + host + ':' + wsport + '/ws');
    var simpleUser = new SIP.Web.SimpleUser('wss://' + host + ':' + wsport + '/ws', options);

    simpleUser.connect();
    simpleUser.register();
    simpleUsers[username] = simpleUser;
}

function callAsterisk(toNumber, username) {
    console.log("Asterisk call to " + 'sip:' + toNumber + '@' + asteriskHost + ' via ' + username);

    if (navigator.getUserMedia) {
        navigator.getUserMedia({audio: true}, function () {
                // Success
                // check if mic is provided
                navigator.mediaDevices.enumerateDevices().then(function (devices) {
                    devices.forEach(function (device) {
                        //if mic is plugged in
                        console.log('simpleUsers.length: ' + simpleUsers.length)
                        if (device.kind === 'audioinput') {
                            simpleUsers[username].call('sip:' + toNumber + '@' + asteriskHost);
                            ringAudio.play();
                        }
                    });
                }).catch(function (err) {
                    console.log(err.name + ": " + err.message);
                });
            },
            function () {
                console.log('No Microphone')
            });
    }
}

function forwardCallAsterisk(toNumber, username) {
    console.log("Asterisk call to " + 'sip:' + toNumber + '@' + asteriskHost + ' via ' + username);

    if (navigator.getUserMedia) {
        navigator.getUserMedia({audio: true}, function () {
                // Success
                // check if mic is provided
                navigator.mediaDevices.enumerateDevices().then(function (devices) {
                    devices.forEach(function (device) {
                        //if mic is plugged in
                        console.log('simpleUsers.length: ' + simpleUsers.length)
                        if (device.kind === 'audioinput') {

                            simpleUsers[username].call('sip:' + toNumber + '@' + asteriskHost,
                                {params: { fromDisplayName: 'Display Name' } },
                                {});

                            ringAudio.play();

                        }
                    });
                }).catch(function (err) {
                    console.log(err.name + ": " + err.message);
                });
            },
            function () {
                console.log('No Microphone')
            });
    }
}

function forwardAsteriskCall(toNumber, username) {
    console.log("Asterisk call Forwarding ! " + username);
    simpleUsers[username].transfer('sip:' + toNumber + '@' + asteriskHost);
}
function answerAsteriskCall(username) {
    console.log("Asterisk call answered ! " + username);
    simpleUsers[username].answer();
}

function hangupAsteriskCall(username) {
    console.log("Asterisk call ended: " + username);
    simpleUsers[username].hangup();
}
function rejectAsteriskCall(username) {
    console.log("Asterisk call rejected: " + username);
    // simpleUsers[username].hangup();
    simpleUsers[username].decline();
}

function isAsteriskConnected(username) {
    console.log("Is Asterisk connected ? " + username);
    return simpleUsers[username].isConnected();
}

function isAsteriskMuted(username) {
    console.log("Is Asterisk muted ? " + username);
    return simpleUsers[username].isMuted();
}

function asteriskMute(username) {
    console.log("Asterisk mute " + username);
    return simpleUsers[username].mute();
}

function asteriskUnmute(username) {
    console.log("Asterisk unmute " + username);
    return simpleUsers[username].unmute();
}

function isAsteriskHeld(username) {
    console.log("Is Asterisk held ? " + username);
    return simpleUsers[username].isHeld();
}

function asteriskHold(username) {
    console.log("Asterisk hold " + username);
    return simpleUsers[username].hold();
}

function asteriskUnhold(username) {
    console.log("Asterisk unhold " + username);
    return simpleUsers[username].unhold();
}

function asteriskSendDTMF(username, tone) {
    console.log("Asterisk sendDTMF " + username);
    return simpleUsers[username].sendDTMF(tone);
}

function asteriskSendMessage(username, destination, message) {
    console.log("Asterisk message " + username);
    return simpleUsers[username].message( destination, message);
}