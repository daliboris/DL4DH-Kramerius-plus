package cz.inqool.dl4dh.krameriusplus.core.domain.security.user;

import cz.inqool.dl4dh.krameriusplus.core.domain.dao.sql.object.DatedObject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class KrameriusUser extends DatedObject {

    @Column(nullable = false)
    private String username;
}
