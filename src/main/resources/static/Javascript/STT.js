var startRecognizeOnceAsyncButton;

// subscription key and region for speech services.
var subscriptionKey, serviceRegion;
var SpeechSDK;
var recognizer;

document.addEventListener("DOMContentLoaded", function () {
startRecognizeOnceAsyncButton = document.getElementById("startRecognizeOnceAsyncButton");
micIcon = document.getElementById("micIcon");
subscriptionKey = "G6WRVtA1qMrvyU4t2GgoUFzAy6UxZLn1TiiHYvl5ZhqN4RG1OJCRJQQJ99BAACqBBLyXJ3w3AAAYACOG5lPJ";
serviceRegion = "southeastasia";
test = document.getElementById("STT")
const searchBar = document.querySelector(".search input"); // Search bar input

startRecognizeOnceAsyncButton.addEventListener("click", function () {
    startRecognizeOnceAsyncButton.disabled = true;
    micIcon.style.color = "red";
    
    if (subscriptionKey.value === "" || subscriptionKey.value === "subscription") {
    alert("Please enter your Microsoft Cognitive Services Speech subscription key!");
    return;
    }
    var speechConfig = SpeechSDK.SpeechConfig.fromSubscription(subscriptionKey, serviceRegion);
    speechConfig.speechRecognitionLanguage = "en-US";
    var audioConfig  = SpeechSDK.AudioConfig.fromDefaultMicrophoneInput();
    recognizer = new SpeechSDK.SpeechRecognizer(speechConfig, audioConfig);

    recognizer.recognizeOnceAsync(
    function (result) {
        startRecognizeOnceAsyncButton.disabled = false;
        micIcon.style.color = "black";
        window.console.log(result);
        test.value = result.text.replace('.', '')
        recognizer.close();
        recognizer = undefined;
        var eventTest = new CustomEvent("input");
        searchBar.dispatchEvent(eventTest);
    },
    function (err) {
        startRecognizeOnceAsyncButton.disabled = false;
        micIcon.style.color = "black";
        test.value = err.text
        window.console.log(err);
        recognizer.close();
        recognizer = undefined;
    });
});

if (!!window.SpeechSDK) {
    SpeechSDK = window.SpeechSDK;
    startRecognizeOnceAsyncButton.disabled = false;

    // document.getElementById('content').style.display = 'block';
    // document.getElementById('warning').style.display = 'none';
}
});
