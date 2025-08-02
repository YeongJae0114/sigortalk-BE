package app.sigorotalk.backend.domain.farmer;

import app.sigorotalk.backend.common.entity.BaseTimeEntity;
import app.sigorotalk.backend.domain.farm_project.FarmProject;
import app.sigorotalk.backend.domain.user.User;
import com.vladmihalcea.hibernate.type.json.JsonType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "farmers")
@Getter
@Setter
@NoArgsConstructor

public class Farmer extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    private String profile;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "farm_location")
    private String farmLocation;

    private String operationExperience;

    @Column(name = "delivery_system")
    private String deliverySystem;

    @Column(name = "cultivation_method")
    private String cultivationMethod;

    @OneToMany(mappedBy = "farmer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FarmProject> farmProjects = new ArrayList<>();
}
