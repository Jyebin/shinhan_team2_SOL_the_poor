package com.choikang.poor.the_poor_back.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Follow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long followID;

    @ManyToOne
    @JoinColumn(name = "toUserID", nullable = false)
    private User toUser;  // 팔로우 당한 사람

    @ManyToOne
    @JoinColumn(name = "fromUserID", nullable = false)
    private User fromUser;  // 팔로우 한 사람
}
