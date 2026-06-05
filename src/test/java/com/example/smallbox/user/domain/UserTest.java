package com.example.smallbox.user.domain;

import com.example.smallbox.shared.domain.Email;
import com.example.smallbox.shared.domain.Phone;
import com.example.smallbox.user.domain.exceptions.UserCreationForbiddenException;
import com.example.smallbox.user.domain.exceptions.UserInvalidRoleException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private final Role superAdminRole = new Role(1, "ROLE_SUPER_ADMIN");
    private final Role branchAdminRole = new Role(2, "ROLE_BRANCH_ADMIN");
    private final Role employeeRole = new Role(3, "ROLE_EMPLOYEE");
    private final Role clientRole = new Role(4, "ROLE_CLIENT");

    @Test
    @DisplayName("Should create employee successfully when creator is SUPER_ADMIN")
    void shouldCreateEmployeeWhenCreatorIsSuperAdmin() {
        User employee = User.createEmployee(
                superAdminRole,
                employeeRole,
                "John",
                null,
                "Doe",
                null,
                new Phone("12345678"),
                new Email("john.doe@example.com"),
                "password123"
        );

        assertNotNull(employee);
        assertEquals(employeeRole, employee.getRole());
        assertEquals("John", employee.getFirstName());
    }

    @Test
    @DisplayName("Should fail when trying to create a CLIENT through createEmployee")
    void shouldFailWhenCreatingClientThroughEmployeeFlow() {
        assertThrows(UserInvalidRoleException.class, () -> 
            User.createEmployee(
                    superAdminRole,
                    clientRole,
                    "John",
                    null,
                    "Doe",
                    null,
                    new Phone("12345678"),
                    new Email("john@example.com"),
                    "password"
            )
        );
    }

    @Test
    @DisplayName("Should fail when creator is not an admin")
    void shouldFailWhenCreatorIsNotAdmin() {
        assertThrows(UserCreationForbiddenException.class, () -> 
            User.createEmployee(
                    employeeRole,
                    employeeRole,
                    "John",
                    null,
                    "Doe",
                    null,
                    new Phone("12345678"),
                    new Email("john@example.com"),
                    "password"
            )
        );
    }

    @Test
    @DisplayName("Should fail when BRANCH_ADMIN tries to create SUPER_ADMIN")
    void shouldFailWhenBranchAdminCreatesSuperAdmin() {
        assertThrows(UserCreationForbiddenException.class, () -> 
            User.createEmployee(
                    branchAdminRole,
                    superAdminRole,
                    "John",
                    null,
                    "Doe",
                    null,
                    new Phone("12345678"),
                    new Email("john@example.com"),
                    "password"
            )
        );
    }

    @Test
    @DisplayName("Should create client successfully through createClient")
    void shouldCreateClientSuccessfully() {
        User client = User.createClient(
                clientRole,
                "Jane",
                null,
                "Doe",
                null,
                new Phone("87654321"),
                new Email("jane.doe@example.com"),
                "password123"
        );

        assertNotNull(client);
        assertEquals(clientRole, client.getRole());
    }

    @Test
    @DisplayName("Should fail when creating non-client through createClient")
    void shouldFailWhenCreatingNonClientThroughClientFlow() {
        assertThrows(UserInvalidRoleException.class, () -> 
            User.createClient(
                    employeeRole,
                    "Jane",
                    null,
                    "Doe",
                    null,
                    new Phone("87654321"),
                    new Email("jane@example.com"),
                    "password"
            )
        );
    }
}
