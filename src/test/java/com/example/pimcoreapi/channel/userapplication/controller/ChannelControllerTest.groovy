package com.example.pimcoreapi.channel.userapplication.controller

import com.example.pimcoreapi.channel.application.channel.CreateChannelUserCase
import com.example.pimcoreapi.channel.application.channel.DeleteChannelUserCase
import com.example.pimcoreapi.channel.application.channel.FindChannelUserCase
import com.example.pimcoreapi.channel.application.channel.UpdateChannelUserCase
import com.example.pimcoreapi.channel.domain.data.channel.CreateChannelDto
import com.example.pimcoreapi.channel.domain.data.channel.ResourceChannelDto
import com.example.pimcoreapi.channel.domain.data.channel.UpdateChannelDto
import com.example.pimcoreapi.shared.exception.data.ErrorResponse
import com.example.pimcoreapi.shared.exception.domain.IsEmptyException
import com.example.pimcoreapi.shared.exception.domain.NotFoundException
import com.example.pimcoreapi.shared.exception.domain.ObjectNullException
import com.fasterxml.jackson.databind.JavaType
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*

//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@AutoConfigureMockMvc
@SpringBootTest
@Slf4j
class ChannelControllerTest extends Specification {

    @SpringBean
    CreateChannelUserCase createChannelUserCase = Mock(CreateChannelUserCase)
    @SpringBean
    DeleteChannelUserCase deleteChannelUserCase = Mock(DeleteChannelUserCase)
    @SpringBean
    FindChannelUserCase findChannelUserCase = Mock(FindChannelUserCase)
    @SpringBean
    UpdateChannelUserCase updateChannelUserCase = Mock(UpdateChannelUserCase)
    @Autowired
    MockMvc mockMvc
    ObjectMapper objectMapper = new ObjectMapper()


    void setup() {
        this.objectMapper.registerModule(new JavaTimeModule())
    }

    def "Post type channel test for successful creation with a valid input and returning an object as a 200 response from the channel"() {
        given:
        def requestBody = new CreateChannelDto([name: "Test"])
        def requestJson = objectMapper.writeValueAsString(requestBody)
        def expectedResponse = new ResourceChannelDto([name: "Test", code: "test"])
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
        1 * createChannelUserCase.unitCreate(requestBody) >> { throw new ObjectNullException("Name") }
        and:
        result.getResponse().getStatus() == 400
        def actualResponseJson = objectMapper.readValue(result.getResponse().getContentAsString(), ErrorResponse.class)
        verifyAll() {
            actualResponseJson.status == 400
            actualResponseJson.error == "Bad Request"
            actualResponseJson.message == "Validation failed"
            actualResponseJson.path == "uri=/api/channel"
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
        1 * deleteChannelUserCase.deleteById(channelId) >> { throw new NotFoundException("id", "Channel") }
        when:
        def result = mockMvc.perform(
                delete("/api/channel/${channelId}")
        ).andReturn()

        then:
        result.getResponse().getStatus() == 404
        def actualResponseJson = objectMapper.readValue(result.getResponse().getContentAsString(), ErrorResponse.class)
        verifyAll {
            actualResponseJson.status == 404
            actualResponseJson.error == "Not Found"
            actualResponseJson.message == "Validation failed"
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

    def "findById channel test for successful search"() {
        given:
        String channelId = "someChannelId"
        def expectedResponse = new ResourceChannelDto([name: "Test", code: "test", id: "id"])
        when:
        def result = mockMvc.perform(
                get("/api/channel/${channelId}")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn()

        then:
        1 * this.findChannelUserCase.findById(channelId) >> expectedResponse
        and:
        result.getResponse().getStatus() == 200
        def actualResponseJson = objectMapper.readValue(result.getResponse().getContentAsString(), ResourceChannelDto.class)
        verifyAll {
            actualResponseJson.name == expectedResponse.name
            actualResponseJson.code == expectedResponse.code
            actualResponseJson.id == expectedResponse.id
        }
    }

    def "findById type channel test for error with a valid input and returning an object as a 4 response from the channel"() {
        given:
        String channelId = "someChannelId"
        when:
        def result = mockMvc.perform(
                get("/api/channel/${channelId}")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn()
        then:
        1 * findChannelUserCase.findById(channelId) >> { throw new NotFoundException(channelId, "Channel") }
        and:
        result.getResponse().getStatus() == 404
        def actualResponseJson = objectMapper.readValue(result.getResponse().getContentAsString(), ErrorResponse.class)
        verifyAll() {
            actualResponseJson.status == 404
            actualResponseJson.error == "Not Found"
            actualResponseJson.message == "Validation failed"
            actualResponseJson.path == "uri=/api/channel/${channelId}"
        }
    }

    def "findById type channel test for error with a valid input and returning an object"() {
        given:
        String channelId = "empty"
        when:
        def result = mockMvc.perform(
                get("/api/channel/${channelId}")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn()
        then:
        1 * findChannelUserCase.findById(channelId) >> { throw new IsEmptyException("id", "Search Channel") }
        and:
        result.getResponse().getStatus() == 400
        def actualResponseJson = objectMapper.readValue(result.getResponse().getContentAsString(), ErrorResponse.class)
        verifyAll() {
            actualResponseJson.status == 400
            actualResponseJson.error == "Bad Request"
            actualResponseJson.message == "Validation failed"
            actualResponseJson.path == "uri=/api/channel/${channelId}"
        }
    }

    def "findAll channel test for successful search"() {
        given:
        def expectedResponse = [
                new ResourceChannelDto([name: "Test", code: "test", id: "id"]),
                new ResourceChannelDto([name: "Test1", code: "test1", id: "id1"]),
                new ResourceChannelDto([name: "Test2", code: "test2", id: "id2"])
        ]
        when:
        def result = mockMvc.perform(
                get("/api/channel")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn()

        then:
        1 * this.findChannelUserCase.findAll() >> expectedResponse
        and:
        result.getResponse().getStatus() == 200
        JavaType type = objectMapper.getTypeFactory().constructCollectionType(List.class, ResourceChannelDto.class)
        List<ResourceChannelDto> actualResponseJson = objectMapper.readValue(result.getResponse().getContentAsString(), type)
        actualResponseJson.size() == expectedResponse.size()
        expectedResponse.eachWithIndex { expected, index ->
            actualResponseJson[index].name == expected.name
            actualResponseJson[index].code == expected.code
            actualResponseJson[index].id == expected.id
        }

    }

    def "Put type channel test for successful creation with a valid input and returning an object as a 200 response from the channel"() {
        given:
        def requestBody = new UpdateChannelDto([name: "Test"])
        def id = "id"
        def requestJson = objectMapper.writeValueAsString(requestBody)
        def expectedResponse = new ResourceChannelDto([name: "Test", code: "test", id: id])
        when:
        def result = mockMvc.perform(
                put("/api/channel/${id}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
        ).andReturn()

        then:
        1 * updateChannelUserCase.updateById(requestBody, id) >> expectedResponse
        and:
        result.getResponse().getStatus() == 200
        def actualResponseJson = objectMapper.readValue(result.getResponse().getContentAsString(), ResourceChannelDto.class)
        verifyAll {
            actualResponseJson.name == expectedResponse.name
            actualResponseJson.code == expectedResponse.code
            actualResponseJson.id == expectedResponse.id
        }
    }

    def "Put type channel test for error creation with a valid input and returning an object as a 4 response from the channel"() {
        given:
        def requestBody = new UpdateChannelDto([name: null])
        def id = "id"
        def requestJson = objectMapper.writeValueAsString(requestBody)
        when:
        def result = mockMvc.perform(
                put("/api/channel/${id}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
        ).andReturn()
        then:
        1 * updateChannelUserCase.updateById(requestBody, id) >> { throw new ObjectNullException("Name") }
        and:
        result.getResponse().getStatus() == 400
        def actualResponseJson = objectMapper.readValue(result.getResponse().getContentAsString(), ErrorResponse.class)
        verifyAll() {
            actualResponseJson.status == 400
            actualResponseJson.error == "Bad Request"
            actualResponseJson.message == "Validation failed"
            actualResponseJson.path == "uri=/api/channel/${id}"
        }
    }
}
