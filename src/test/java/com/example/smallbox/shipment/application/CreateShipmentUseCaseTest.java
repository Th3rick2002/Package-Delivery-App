package com.example.smallbox.shipment.application;

import com.example.smallbox.shared.domain.UserId;
import com.example.smallbox.shipment.application.dto.CreateShipmentRequest;
import com.example.smallbox.shipment.domain.port.ShipmentRepository;
import com.example.smallbox.shipment.domain.service.ShipmentDomainService;
import com.example.smallbox.user.domain.Role;
import com.example.smallbox.user.domain.User;
import com.example.smallbox.user.domain.UserRepository;
import com.example.smallbox.user.domain.exceptions.UserInvalidRoleException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class CreateShipmentUseCaseTest {

    private UserRepository userRepository;
    private ShipmentRepository shipmentRepository;
    private ShipmentDomainService shipmentDomainService;
    private CreateShipmentUseCase createShipmentUseCase;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        shipmentRepository = mock(ShipmentRepository.class);
        shipmentDomainService = mock(ShipmentDomainService.class);
        createShipmentUseCase = new CreateShipmentUseCase(shipmentRepository, userRepository, shipmentDomainService);
    }

    @Test
    void execute_ThrowsUserInvalidRoleExceptionWhenSenderIsSuperAdmin() {
        UUID senderUuid = UUID.randomUUID();
        CreateShipmentRequest request = new CreateShipmentRequest(
                senderUuid,
                new CreateShipmentRequest.RecipientRequest("Carlos", null, "Ramirez", null, "+50377777777", "carlos@example.com"),
                1,
                2,
                List.of()
        );

        User superAdmin = User.builder()
                .id(new UserId(senderUuid))
                .role(Role.fromName("ROLE_SUPER_ADMIN"))
                .build();

        when(userRepository.findById(any())).thenReturn(Optional.of(superAdmin));

        assertThrows(UserInvalidRoleException.class, () -> createShipmentUseCase.execute(request));
    }

    @Test
    void execute_ThrowsUserInvalidRoleExceptionWhenSenderIsBranchAdmin() {
        UUID senderUuid = UUID.randomUUID();
        CreateShipmentRequest request = new CreateShipmentRequest(
                senderUuid,
                new CreateShipmentRequest.RecipientRequest("Carlos", null, "Ramirez", null, "+50377777777", "carlos@example.com"),
                1,
                2,
                List.of()
        );

        User branchAdmin = User.builder()
                .id(new UserId(senderUuid))
                .role(Role.fromName("ROLE_BRANCH_ADMIN"))
                .build();

        when(userRepository.findById(any())).thenReturn(Optional.of(branchAdmin));

        assertThrows(UserInvalidRoleException.class, () -> createShipmentUseCase.execute(request));
    }
}
