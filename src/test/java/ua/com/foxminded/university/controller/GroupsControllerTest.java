package ua.com.foxminded.university.controller;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.MediaType;

import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.service.GroupService;

public class GroupsControllerTest extends AbstractControllerTest {
    @Mock
    GroupService groupServiceMock;   
    @InjectMocks
    GroupsController groupsController;
    
    @Test
    public void checkMethodGetAllShouldShowAllGroups() throws Exception {
        Mockito.when(groupServiceMock.getAllGroups()).thenReturn(createFakeGroupsList());
        groupServiceMock.getAllGroups();
        mockMvc.perform(get("/groups")).andDo(print())
        .andExpect(status().isOk())
                .andExpect(view().name("groups/index"))
                .andExpect(model().attributeExists("group"))
                .andExpect(model().attribute("group", hasSize(20)))
                .andExpect(model().attribute("group", hasItem(allOf(hasProperty("id", is(1)), hasProperty("name", is("QW-11"))))))
                .andExpect(model().attribute("group", hasItem(allOf(hasProperty("id", is(2)), hasProperty("name", is("ER-12"))))))
                .andExpect(model().attribute("group", hasItem(allOf(hasProperty("id", is(3)), hasProperty("name", is("TY-21"))))));
        Mockito.verify(groupServiceMock, times(1)).getAllGroups();
        Mockito.verifyNoMoreInteractions(groupServiceMock);
    }
    
    @Test
    public void checkMethodGetShouldShowNecessaryGroup() throws Exception {
        Mockito.when(groupServiceMock.getGroup(1)).thenReturn(createFakeGroup());
        groupServiceMock.getGroup(1);
        mockMvc.perform(get("/groups/show/1")).andDo(print())
        .andExpect(status().isOk())
                .andExpect(view().name("groups/show"))
                .andExpect(model().attributeExists("group"))
                .andExpect(model().attribute("group", hasProperty("id", is(1))))
                .andExpect(model().attribute("group", hasProperty("name", is("QW-11"))));
        Mockito.verify(groupServiceMock, times(1)).getGroup(1);
        Mockito.verifyNoMoreInteractions(groupServiceMock);
    }
    
    @Test
    public void checkMethodCreateFormShouldOpenFormForCreateGroup() throws Exception {
        mockMvc.perform(get("/groups/new")).andDo(print())
        .andExpect(status().isOk())
                .andExpect(view().name("groups/new"));
    }
    
    @Test
    public void checkMethodCreateShouldCreateGroup() throws Exception {
        groupServiceMock.saveGroup(Arrays.asList(createFakeGroup()));
        mockMvc.perform(post("/groups")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", "FF-22")
                .sessionAttr("group", new Group()))
                .andExpect(view().name("redirect:/groups"));              
        ArgumentCaptor<Group> argumentCaptor = ArgumentCaptor.forClass(Group.class);
        Mockito.verify(groupServiceMock, times(1)).saveGroup(Arrays.asList(argumentCaptor.capture()));
        Mockito.verifyNoMoreInteractions(groupServiceMock);
    }
    
    @Test
    public void checkMethodUpdateFormShouldOpenNecessaryGroup() throws Exception {
        Mockito.when(groupServiceMock.getGroup(1)).thenReturn(createFakeGroup());
        groupServiceMock.getGroup(1);
        mockMvc.perform(get("/groups/edit/1")).andDo(print())
        .andExpect(status().isOk())
                .andExpect(view().name("groups/edit"))
                .andExpect(model().attributeExists("groupDtoIn"))
                .andExpect(model().attribute("groupDtoIn", hasProperty("id", is(1))))
                .andExpect(model().attribute("groupDtoIn", hasProperty("name", is("QW-11"))));
        Mockito.verify(groupServiceMock, times(1)).getGroup(1);
        Mockito.verifyNoMoreInteractions(groupServiceMock);
    }
    
    @Test
    public void checkMethodUpdateShouldUpdateNecessaryGroup() throws Exception {
        groupServiceMock.updateGroup(createFakeGroup());
        mockMvc.perform(post("/groups/edit/1?")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", "QW-11")).
        andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/groups"));
        ArgumentCaptor<Group> argumentCaptor = ArgumentCaptor.forClass(Group.class);
        Mockito.verify(groupServiceMock, times(1)).updateGroup(argumentCaptor.capture());
        Mockito.verifyNoMoreInteractions(groupServiceMock);
    }
    
    @Test
    public void checkMethodDeleteShouldDeleteNecessaryGroup() throws Exception {
        mockMvc.perform(get("/groups/delete/11")).andDo(print())
        .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/groups"));
    }
    
    @Test
    public void checkMethodGetShouldThrowExceptionNecessaryGroup() throws Exception {
        mockMvc.perform(get("/groups/show/111")).andDo(print()).andExpect(status().isOk())
                .andExpect(view().name("groups/error"));
    }
    
    @Test
    public void checkMethodCreateShouldThrowException() throws Exception {
        mockMvc.perform(post("/groups?name")).andDo(print()).andExpect(status().isOk())
                .andExpect(view().name("groups/new"));
    }
    
    @Test
    public void checkMethodUpdateFormShouldThrowExceptionNecessaryGroup() throws Exception {
        mockMvc.perform(get("/groups/edit/111")).andDo(print()).andExpect(status().isOk())
                .andExpect(view().name("groups/error"));
    }
    
    @Test
    public void checkMethodUpdateShouldThrowExceptionNecessaryGroup() throws Exception {
        mockMvc.perform(post("/groups/edit/11?name=")).andDo(print()).andExpect(status().isOk())
                .andExpect(view().name("groups/edit"));
    }
    
    @Test
    public void checkMethodDeleteShouldThrowExceptionNecessaryGroup() throws Exception {
        mockMvc.perform(get("/groups/delete/111")).andDo(print()).andExpect(status().isOk())
                .andExpect(view().name("groups/error"));
    }
    
    private Group createFakeGroup() {
        return Group.builder().id(1).name("QW-11").build();
    }
    
    private List<Group> createFakeGroupsList() {
        return Arrays.asList(Group.builder().id(1).name("QW-11").build(),
                Group.builder().id(2).name("ER-12").build(), Group.builder().id(3).name("TY-21").build());
    }
}
