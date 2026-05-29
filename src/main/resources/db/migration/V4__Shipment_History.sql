-- Tabla de historial de cambios de estado de envíos (append-only)
-- No tiene columnas updated_at/deleted_at para reforzar la inmutabilidad
CREATE TABLE shipment_history (
    history_id  BIGSERIAL   PRIMARY KEY,
    shipment_id BIGINT      NOT NULL REFERENCES shipment(shipment_id),
    status_id   INT         NOT NULL REFERENCES shipment_status(status_id),
    changed_by  UUID        NOT NULL REFERENCES users(user_id),
    comments    TEXT,
    created_at  TIMESTAMP   NOT NULL DEFAULT now()
);
