package sample;

import java.util.Map;
import javax.swing.JOptionPane;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.slu.Slot;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.LaunchRequest;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SessionEndedRequest;
import com.amazon.speech.speechlet.SessionStartedRequest;
import com.amazon.speech.speechlet.Speechlet;
import com.amazon.speech.speechlet.SpeechletException;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.Reprompt;
import com.amazon.speech.ui.SimpleCard;

/**
 * This sample shows how to create a simple speechlet for handling intent requests and managing
 * session interactions.
 */
public class SampleHaus implements Speechlet {
    private static final Logger log = LoggerFactory.getLogger(SampleHaus.class);

    @Override
    public void onSessionStarted(final SessionStartedRequest request, final Session session)
            throws SpeechletException {
        log.info("onSessionStarted requestId={}, sessionId={}", request.getRequestId(),
                session.getSessionId());
        // any initialization logic goes here
    }

    @Override
    public SpeechletResponse onLaunch(final LaunchRequest request, final Session session)
            throws SpeechletException {
        log.info("onLaunch requestId={}, sessionId={}", request.getRequestId(),
                session.getSessionId());
        return getWelcomeResponse();
    }

    @Override
    public SpeechletResponse onIntent(final IntentRequest request, final Session session)
            throws SpeechletException {
        log.info("onIntent requestId={}, sessionId={}", request.getRequestId(),
                session.getSessionId());

        // Get intent from the request object.
        Intent intent = request.getIntent();
        String intentName = (intent != null) ? intent.getName() : null;

        // Note: If the session is started with an intent, no welcome message will be rendered;
        // rather, the intent specific response will be returned.
        if ("CheckHouseTemperature".equals(intentName)) {
            return CheckHouseTemperature(intent, session);
        } else if ("SetHouseTemp".equals(intentName)) {
            return SetHouseTemp(intent, session);
        } else if ("SetTemp".equals(intentName)) {
            return SetTemp(intent, session);
        } else {
            throw new SpeechletException("Invalid Intent");
        }
    }

    @Override
    public void onSessionEnded(final SessionEndedRequest request, final Session session)
            throws SpeechletException {
        log.info("onSessionEnded requestId={}, sessionId={}", request.getRequestId(),
                session.getSessionId());
        // any cleanup logic goes here
    }

    /**
     * Creates and returns a {@code SpeechletResponse} with a welcome message.
     *
     * @return SpeechletResponse spoken and visual welcome message
     */
    private SpeechletResponse getWelcomeResponse() {
        String speechText =
                "Welcome to the Alexa Skills Kit sample. You can perform a variety of house functions with me";

        return getSpeechletResponse(speechText);
    }

    private SpeechletResponse CheckHouseTemperature(final Intent intent, final Session session) {
        int temp = 67;// mqtt server interaction here
        String response = "The temperature of the house is " + temp + " degrees.";
        return getSpeechletResponse(response);
    }

    private SpeechletResponse SetHouseTemp(final Intent intent, final Session session) {
        
        Map<String, Slot> slots = intent.getSlots();
        // Get the color slot from the list of slots.
        Slot t = slots.get("HouseTemperature");
        int temp = Integer.parseInt(t.getValue());
        // JOptionPane.showMessageDialog(null, "The house temperature has been set to " + temp + " degrees");
        // mqtt server interaction here, set the temperature of the house. Create a method that is asynchronous

        String response = "The temperature of the house has been set to " + temp + " degrees.";
        return getSpeechletResponse(response);
    }

    private SpeechletResponse SetTemp(final Intent intent, final Session session) {
        
        Map<String, Slot> slots = intent.getSlots();
        // Get the color slot from the list of slots.
        Slot t = slots.get("Temperature");
        int temp = Integer.parseInt(t.getValue());
        Slot obj = slots.get("Object");
        String object = obj.getValue();
        // mqtt server interaction here, set the temperature of the house. Create a method that is asynchronous
        
        String response = "The temperature of the " + object + " has been set to " + temp + " degrees.";
        return getSpeechletResponse(response);
    }

    /**
     * Returns a Speechlet response for a speech and reprompt text.
     */
    private SpeechletResponse getSpeechletResponse(String speechText) {
        // Create the Simple card content.
        SimpleCard card = new SimpleCard();
        card.setTitle("Session");
        card.setContent(speechText);

        // Create the plain text output.
        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(speechText);

        return SpeechletResponse.newTellResponse(speech, card);
    }
}