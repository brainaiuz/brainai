const kpiChatWrapper = document.createElement("div");
kpiChatWrapper.id = "kpiChat-wrapper";
// kpiChatWrapper.style.zIndex = "1";
// kpiChatWrapper.style.background = "transparent";
// kpiChatWrapper.style.overflow = "hidden";
// kpiChatWrapper.style.position = "fixed";

// kpiChatWrapper.style.bottom = "var(--frameHelpChatPos-bottom)";
// kpiChatWrapper.style.right = "var(--frameHelpChatPos-right)";
kpiChatWrapper.style.width = "0";
kpiChatWrapper.style.height = "0";
// kpiChatWrapper.style.borderRadius = "0";
const blockHeight = getComputedStyle(document.documentElement).getPropertyValue('--block-height');

const kpiChatScript = document.querySelector(
    "script[data-name='kpi-chatbot']"
);
let widgetSize = kpiChatScript?.getAttribute("data-widget-size");
let widgetOpen = false;

document.body.appendChild(kpiChatWrapper);

const iframe = document.createElement("iframe");

function getIframeUrl() {
    const iframUrl = 'https://kpi-chatbot.vercel.app/'
    // const iframUrl = 'http://localhost:5173/'
    iframe.allow = "microphone; fullscreen";
    const urlObj = new URL(iframUrl);
    return urlObj.toString();
}

let iframeUrl = getIframeUrl();
iframe.setAttribute("src", iframeUrl);

iframe.setAttribute("allow", "microphone; fullscreen");
iframe.setAttribute("frameborder", "0");
iframe.setAttribute("scrolling", "no");
iframe.style.width = "100%";
iframe.style.height = "100%";
iframe.style.background = "transparent";
iframe.id = "kpiChat";

// kpiChatWrapper.style.zIndex = "1";
kpiChatWrapper?.appendChild(iframe);

const VALID_WIDGET_SIZES = {
    small: {
        height: '490px',
        width: '320px'
    },
    large: {
        height: '642px',
        width: '420px'
    }
}

if (!VALID_WIDGET_SIZES[widgetSize]) {
    widgetSize = "large";
}


function waitForElm(selector) {
    return new Promise((resolve) => {
        if (document.querySelector(selector)) {
            return resolve(document.querySelector(selector));
        }

        const observer = new MutationObserver((mutations) => {
            if (document.querySelector(selector)) {
                resolve(document.querySelector(selector));
                observer.disconnect();
            }
        });

        observer.observe(document.body, {
            childList: true,
            subtree: true,
        });
    });
}

waitForElm("#kpiChat").then((elm) => {
    window.addEventListener(
        "message",
        function (e) {
            let type;
            let payload;

            try {
                const parsed = JSON.parse(e.data);
                type = parsed?.type;
                payload = parsed?.payload;
            } catch (err) {
                return;
            }

            switch (type) {
                case "showChat": {
                    if (payload.isVisible === true) {
                        if (window.matchMedia("(max-width: 800px)").matches) {
                            iframe.parentNode.style.height = `min(100%, ${VALID_WIDGET_SIZES['small'].height})`;
                            iframe.parentNode.style.width = `min(100%, ${VALID_WIDGET_SIZES['small'].width})`;
                            document.body.style.overflow = "auto";
                        } else if (window.matchMedia("(min-width: 800px)").matches) {
                            kpiChatWrapper.style.height = `min(100%, ${VALID_WIDGET_SIZES[widgetSize].height})`;
                            kpiChatWrapper.style.width = `min(100%, ${VALID_WIDGET_SIZES[widgetSize].width})`;
                            // kpiChatWrapper.style.borderRadius = "8px";
                            document.body.style.overflow = "auto";
                        } else {
                            kpiChatWrapper.style.height = "100%";
                            kpiChatWrapper.style.width = "100%";
                            // kpiChatWrapper.style.borderRadius = "8px";
                            document.body.style.setProperty(
                                "overflow",
                                "hidden",
                                "important"
                            );
                        }
                        widgetOpen = true;
                    } else {
                        kpiChatWrapper.style.height = "0";
                        kpiChatWrapper.style.width ="0";
                        kpiChatWrapper.style.borderRadius = "0";
                        document.body.style.overflow = "auto";
                        widgetOpen = false;
                    }
                    break;
                }
                case "microphonePressed": {
                    navigator.mediaDevices.getUserMedia({ audio: true })
                        .then((stream) => {
                            console.log("Microphone access granted:", stream);
                        })
                        .catch((error) => {
                            console.error("Microphone access denied:", error);
                        });
                    break;
                }
            }
        },
        false
    );
});

window.isKpiChatOpen = function () {
    return widgetOpen;
};


    window.setToken = (token) => {
        document
            .querySelector("#kpiChat")
            .contentWindow.postMessage(
            { action: "setToken", value: token },
            "*"
        );
    };

    window.setDomain = (host) => {
        document.querySelector("#kpiChat").contentWindow.postMessage(
            {
                action: "setDomain",
                value: host,
            },
            "*"
        );
    };




// add <style> to override tawk.to styles
    const style = document.createElement('style');
    style.innerHTML = `
  .tawkto-css#tawk_5b2e498aeba8cd3125e31c0d .widget-visible   {
    overflow: hidden !important;
    border-radius: var(--helpChatBorderRadius) var(--helpChatBorderRadius) 0 0 !important;
    // border: 1px solid var(--helpChatBorderColor) !important;
    // border-bottom: none !important;
  }`;
    document.head.appendChild(style);
