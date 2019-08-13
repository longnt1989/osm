package com.pt.osm.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "user")
@Data
@NoArgsConstructor
@ToString
@AllArgsConstructor
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;
	@Column(name = "parent_id")
	private Long parentId;
	private String username;
    private String email;
	private String skype;
    @Column(name = "password")
    private String password;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
	@Column(name = "user_type")
	private String userType;
	private String side;
    private int status;

	public static enum Side {

		VN("VIET NAM"), GE ("GERMANY");

		private final String value;

		private Side(String value) {
			this.value = value;
		}

		public String value() {
			return this.value;
		}

		public static Side getEnum(String side) {
			for (Side subside : values()) {
				if (subside.value == side) {
					return subside;
				}
			}
			throw new IllegalArgumentException("No matching constant for [" + side + "]");
		}
	}

	public static enum UserType {

		ADMIN("ADMIN"),GERMAN("GERMAN"), CEO ("CEO"), TEAMLEADER ("TEAM LEADER"), GROUPLEADER ("GROUP LEADER"), ENGINEER ("ENGINEER");

		private final String value;

		private UserType(String value) {
			this.value = value;
		}

		public String value() {
			return this.value;
		}

		public static UserType getEnum(String userType) {
			for (UserType subside : values()) {
				if (subside.value == userType) {
					return subside;
				}
			}
			throw new IllegalArgumentException("No matching constant for [" + userType + "]");
		}
	}

    public static enum Status {

        ACTIVE(1), INACTIVE (0);

        private final int value;

        private Status(int value) {
            this.value = value;
        }

        public int value() {
            return this.value;
        }

        public static Status getEnum(int status) {
            for (Status subStatus : values()) {
                if (subStatus.value == status) {
                    return subStatus;
                }
            }
            throw new IllegalArgumentException("No matching constant for [" + status + "]");
        }
    }

}
