package com.mkdevelopers.accountify.business.service;

import com.mkdevelopers.accountify.business.dto.BusinessDto;
import com.mkdevelopers.accountify.business.dto.CreateBusinessRequest;
import com.mkdevelopers.accountify.business.dto.UpdateBusinessRequest;
import com.mkdevelopers.accountify.business.entity.BusinessEntity;
import com.mkdevelopers.accountify.business.exception.BusinessNotFoundException;
import com.mkdevelopers.accountify.business.mapper.BusinessMapper;
import com.mkdevelopers.accountify.business.repository.BusinessRepository;
import com.mkdevelopers.accountify.common.utils.SecurityUtils;
import com.mkdevelopers.accountify.user.entity.UserEntity;
import com.mkdevelopers.accountify.user.repository.UserRepository;
import com.mkdevelopers.accountify.user.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BusinessServiceTest {

    @Mock
    private BusinessRepository businessRepository;
    @Mock
    private BusinessMapper businessMapper;
    @Mock
    private UserRepository userRepository;

    private MockedStatic<SecurityUtils> security;
    private BusinessService businessService;

    @BeforeEach
    void setUp() {
        security = mockStatic(SecurityUtils.class);
        security.when(SecurityUtils::getCurrentUserId).thenReturn("user-a");
        businessService = new BusinessService(
                businessRepository,
                businessMapper,
                userRepository,
                new UserService(userRepository));
    }

    @AfterEach
    void tearDown() {
        security.close();
    }

    @Test
    void getAndUpdateRejectAnotherUsersBusiness() {
        var owner = new UserEntity();
        owner.setUserId("user-b");
        var business = new BusinessEntity();
        business.setUser(owner);
        when(businessRepository.findById("b1")).thenReturn(Optional.of(business));

        assertThrows(BusinessNotFoundException.class, () -> businessService.getBusinessById("b1"));
        assertThrows(BusinessNotFoundException.class,
                () -> businessService.updateBusiness("b1", new UpdateBusinessRequest()));
    }

    @Test
    void createUpsertsUserWhenMissing() {
        security.when(() -> SecurityUtils.getClaim(eq("email"), anyString())).thenReturn("a@example.com");
        security.when(() -> SecurityUtils.getClaim(eq("name"), anyString())).thenReturn("Ada");

        var request = new CreateBusinessRequest();
        request.setBusinessId("b1");
        var entity = new BusinessEntity();

        when(userRepository.findById("user-a")).thenReturn(Optional.empty());
        when(userRepository.save(any(UserEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(businessRepository.existsById("b1")).thenReturn(false);
        when(businessMapper.toEntity(request)).thenReturn(entity);
        when(businessMapper.toDto(entity)).thenReturn(new BusinessDto("b1", "Shop", null, "City", 1L));

        businessService.createBusiness(request);

        var savedUser = ArgumentCaptor.forClass(UserEntity.class);
        verify(userRepository).save(savedUser.capture());
        assertEquals("user-a", savedUser.getValue().getUserId());
        assertEquals("a@example.com", savedUser.getValue().getEmail());
        assertEquals("Ada", savedUser.getValue().getProfileName());
        assertEquals(savedUser.getValue(), entity.getUser());
    }
}
