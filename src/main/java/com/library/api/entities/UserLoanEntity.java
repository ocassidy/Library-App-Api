package com.library.api.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Calendar;
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

    @Column(name = "fine")
    private Integer fine;

    @Column(name = "cost_per_day")
    private Integer costPerDay;

    @Column(name = "overdue_by")
    private Integer overdueBy;

    @CreationTimestamp
    @Column(name = "date_withdrawn")
    private Calendar dateWithdrawn;

    @Column(name = "date_due_back")
    private Calendar dateDueBack;

    @Column(name = "date_returned")
    private Calendar dateReturned;

    @NotNull
    @Column(name = "is_active")
    private boolean isActive;
}
