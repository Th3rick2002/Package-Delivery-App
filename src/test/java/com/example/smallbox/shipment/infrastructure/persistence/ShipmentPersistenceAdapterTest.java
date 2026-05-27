package com.example.smallbox.shipment.infrastructure.persistence;

import com.example.smallbox.shared.domain.BranchID;
import com.example.smallbox.shared.domain.Email;
import com.example.smallbox.shared.domain.LocationId;
import com.example.smallbox.shared.domain.Phone;
import com.example.smallbox.shared.domain.UserId;
import com.example.smallbox.shipment.domain.Package;
import com.example.smallbox.shipment.domain.Recipient;
import com.example.smallbox.shipment.domain.Shipment;
import com.example.smallbox.shipment.domain.port.ShipmentRepository;
import com.example.smallbox.shipment.domain.vo.Dimensions;
import com.example.smallbox.shipment.domain.vo.Weight;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
class ShipmentPersistenceAdapterTest {
    @Autowired
    private ShipmentRepository shipmentRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private EntityManager entityManager;

    @Test
    void savePersistsShipmentRecipientAndPackagesAtomically() {
        UUID senderId = jdbcTemplate.queryForObject(
                "SELECT user_id FROM users WHERE email = ?",
                UUID.class,
                "admin@smallbox.com"
        );
        Integer originBranchId = createBranch("ORIGIN TEST BRANCH", 28, "70000001");
        Integer destinationBranchId = createBranch("DESTINATION TEST BRANCH", 29, "70000002");

        Shipment shipment = Shipment.create(
                new UserId(senderId),
                Recipient.create(
                        "Maria",
                        null,
                        "Lopez",
                        null,
                        new Phone("77777777"),
                        new Email("maria@example.com")
                ),
                new LocationId(29),
                "Final street #42",
                new BranchID(originBranchId),
                new BranchID(destinationBranchId),
                List.of(
                        Package.create("Documents", Weight.ofKg(new BigDecimal("1.25")), Dimensions.ofCm(30, 20, 2)),
                        Package.create("Small box", Weight.ofKg(new BigDecimal("4.00")), Dimensions.ofCm(40, 30, 20))
                ),
                "SB"
        );

        Shipment saved = shipmentRepository.save(shipment);
        entityManager.flush();
        entityManager.clear();

        assertNotNull(saved.shipmentId());
        assertNotNull(saved.recipient().recipientId());
        assertEquals(2, saved.packages().size());
        assertTrue(saved.packages().stream().allMatch(packageItem -> packageItem.packageId() != null));

        Integer packageCount = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM package WHERE shipment_id = ?",
                Integer.class,
                saved.shipmentId()
        );
        Integer recipientCount = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM recipient WHERE recipient_id = ?",
                Integer.class,
                saved.recipient().recipientId()
        );

        assertEquals(2, packageCount);
        assertEquals(1, recipientCount);
    }

    private Integer createBranch(String name, Integer departmentId, String phone) {
        return jdbcTemplate.queryForObject(
                "INSERT INTO branch (name_branch, department_id, phone_branch, created_at) VALUES (?, ?, ?, now()) RETURNING branch_id",
                Integer.class,
                name,
                departmentId,
                phone
        );
    }
}
