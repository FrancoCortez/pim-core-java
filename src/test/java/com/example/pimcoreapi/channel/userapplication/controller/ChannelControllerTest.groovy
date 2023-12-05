package com.example.pimcoreapi.channel.userapplication.controller

import com.example.pimcoreapi.channel.application.channel.CreateChannelUserCase
import com.example.pimcoreapi.channel.application.channel.DeleteChannelUserCase
import com.example.pimcoreapi.channel.domain.data.channel.CreateChannelDto
import com.example.pimcoreapi.channel.domain.data.channel.ResourceChannelDto
import com.example.pimcoreapi.shared.exception.data.ErrorResponse
import com.example.pimcoreapi.shared.exception.domain.NotFoundException
import com.example.pimcoreapi.shared.exception.domain.ObjectNullException
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import lombok.extern.slf4j.Slf4j
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post

//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@AutoConfigureMockMvc
@SpringBootTest
@Slf4j
class ChannelControllerTest extends Specification {

    @SpringBean
    CreateChannelUserCase createChannelUserCase = Mock(CreateChannelUserCase)
    @SpringBean
    DeleteChannelUserCase deleteChannelUserCase = Mock(DeleteChannelUserCase)
    @Autowired
    MockMvc mockMvc
    ObjectMapper objectMapper = new ObjectMapper()


    void setup() {
        this.objectMapper.registerModule(new JavaTimeModule())
    }

    def "Post type channel test for successful creation with a valid input and returning an object as a 200 response from the channel"() {
        given:
        def requestBody = new CreateChannelDto([name: 'Test'])
        def requestJson = objectMapper.writeValueAsString(requestBody)
        def expectedResponse = new ResourceChannelDto([name: 'Test', code: 'test'])
        when:
        def result = mockMvc.perform(
                post("/api/channel")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
        ).andReturn()

        then:
        1 * createChannelUserCase.unitCreate(requestBody) >> expectedResponse
        and:
        result.getResponse().getStatus() == 200
        def actualResponseJson = objectMapper.readValue(result.getResponse().getContentAsString(), ResourceChannelDto.class)
        verifyAll {
            actualResponseJson.name == expectedResponse.name
            actualResponseJson.code == expectedResponse.code
        }
    }

    def "Post type channel test for error creation with a valid input and returning an object as a 4 response from the channel"() {
        given:
        def requestBody = new CreateChannelDto([name: null])
        def requestJson = objectMapper.writeValueAsString(requestBody)
        when:
        def result = mockMvc.perform(
                post("/api/channel")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
        ).andReturn()
        then:
        1 * createChannelUserCase.unitCreate(requestBody) >> { throw new ObjectNullException('Name') }
        and:
        result.getResponse().getStatus() == 400
        def actualResponseJson = objectMapper.readValue(result.getResponse().getContentAsString(), ErrorResponse.class)
        verifyAll() {
            actualResponseJson.status == 400
            actualResponseJson.error == 'Bad Request'
            actualResponseJson.message == 'Validation failed'
            actualResponseJson.path == 'uri=/api/channel'
        }
    }

    def "Delete channel test for successful deletion"() {
        given:
        String channelId = "someChannelId"

        when:
        def result = mockMvc.perform(
                delete("/api/channel/${channelId}")
        ).andReturn()

        then:
        1 * deleteChannelUserCase.deleteById(channelId)
        result.getResponse().getStatus() == 200
    }

    def "Delete channel test for handling not found exception"() {
        given:
        String channelId = "nonExistentChannelId"
        1 * deleteChannelUserCase.deleteById(channelId) >> { throw new NotFoundException('id', 'Channel') }

        when:
        def result = mockMvc.perform(
                delete("/api/channel/${channelId}")
        ).andReturn()

        then:
        result.getResponse().getStatus() == 404
        def actualResponseJson = objectMapper.readValue(result.getResponse().getContentAsString(), ErrorResponse.class)
        verifyAll {
            actualResponseJson.status == 404
            actualResponseJson.error == 'Not Found'
            actualResponseJson.message == 'Validation failed'
            actualResponseJson.path == "uri=/api/channel/${channelId}"
        }
    }

    def "Delete all channel test for successful deletion"() {
        when:
        def result = mockMvc.perform(
                delete("/api/channel/all/delete")
        ).andReturn()

        then:
        1 * deleteChannelUserCase.deleteAll()
        result.getResponse().getStatus() == 200
    }
}
