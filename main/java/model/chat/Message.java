package model.chat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import model.User;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @JoinColumn(nullable = false)
    @ManyToOne
    private Chat chat;

    private Date sendDate;

    @JoinColumn(nullable = false)
    @ManyToOne
    private User Sender;

    private String message;
}
