package com.marvastsi.demo.user;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import com.marvastsi.demo.auth.UserAuth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Audited
@AuditOverride(forClass = UserAuth.class, isAudited = true)
@AuditTable(value = "user_audit")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "USER", schema = "public")
public class User extends UserAuth {
	private static final long serialVersionUID = 1L;

	@Column(name = "name")
	private String name;

	@NotAudited
	private String notAuditedProperty = "This property is not audit by envers";

	@Transient
	private String notPersistedProperty = "This property is not persisted by JPA";

}
