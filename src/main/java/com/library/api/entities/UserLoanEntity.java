package com.library.api.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_loans")
public class UserLoanEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "loan_id")
    private Long id;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "userLoan")
    private List<BookLoanEntity> bookLoans;

    @JsonBackReference("userEntity")
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "user_id", nullable = false, referencedColumnName = "user_id")
    private UserEntity userEntity;

//    @Column(name = "fine")
//    private String fine;
//
//    @Column(name = "cost_per_day")
//    private String costPerDay;
//
//    @Column(name = "overdue_by")
//    private String overdueBy;
}
