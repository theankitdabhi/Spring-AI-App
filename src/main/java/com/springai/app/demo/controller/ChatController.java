package com.springai.app.demo.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.image.Image;
import org.springframework.ai.image.ImageModel;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.ai.openai.OpenAiImageOptions;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
class ChatController {

    private final ChatClient chatClient;

    private final ImageModel imageModel;

    public ChatController(ChatClient.Builder chatClientBuilder, ImageModel imageModel) {
        this.chatClient = chatClientBuilder.build();
        this.imageModel = imageModel;
    }

    @GetMapping("/generation")
    String generation(String userInput) {
        return this.chatClient.prompt()
            .user(userInput)
            .call()
            .content();
    }

    @GetMapping("/summarizeText")
    String summarizeText(String text) {
        String prompt = "Summarize the following text:\n" + text;
        return chatClient.prompt(new Prompt(prompt)).call().content();
    }
    @GetMapping("/translateText")
    String translateText(String text, String language) {
        String prompt = "Translate the following text to " + language + ":\n" + text;
        return chatClient.prompt(new Prompt(prompt)).call().content();
    }
    @GetMapping("/sentiment")
    String sentiment(String text) {
        String prompt = "Analyze the sentiment of the following text and respond with Positive, Negative, or Neutral:\n" + text;
        return chatClient.prompt(new Prompt(prompt)).call().content();
    }

    @GetMapping("/image/{prompt}")
    public Image getImage(@PathVariable String prompt){
        ImageResponse response = imageModel.call(
                new ImagePrompt(prompt,
                        OpenAiImageOptions.builder()
                                .withQuality("hd")
                                .withN(4)
                                .withHeight(1024)
                                .withWidth(1024).build())

        );
        return response.getResult().getOutput();
    }
}