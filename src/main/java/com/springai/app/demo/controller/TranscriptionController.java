package com.springai.app.demo.controller;

import org.springframework.ai.audio.transcription.AudioTranscriptionPrompt;
import org.springframework.ai.audio.transcription.AudioTranscriptionResponse;
import org.springframework.ai.openai.OpenAiAudioTranscriptionModel;
import org.springframework.ai.openai.OpenAiAudioTranscriptionOptions;
import org.springframework.ai.openai.api.OpenAiAudioApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ai")
public class TranscriptionController {

    private final OpenAiAudioTranscriptionModel transcriptionModel;

    public TranscriptionController() {
        OpenAiAudioTranscriptionOptions options = OpenAiAudioTranscriptionOptions.builder()
                .language("en")
                .prompt("Create transcription for this audio file.")
                .temperature(0f)
                .responseFormat(OpenAiAudioApi.TranscriptResponseFormat.TEXT)
                .build();

        var openAiAudioApi = new OpenAiAudioApi("api.key");
        this.transcriptionModel = new OpenAiAudioTranscriptionModel(openAiAudioApi, options);

    }

    @GetMapping("/transcription")
    public String transcribeAudio(@Value("classpath:speech.mp3") Resource audioFile) {
        AudioTranscriptionResponse response = transcriptionModel.call(new AudioTranscriptionPrompt(audioFile));
        return response.getResult().getOutput();
    }
}
