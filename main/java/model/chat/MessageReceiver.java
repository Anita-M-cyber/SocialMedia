package model.chat;

import lombok.*;
import model.User;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MessageReceiver {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private Date receiverDate;

    @ManyToOne
    private Message message;

    @ManyToOne
    private User receiver;

    private Boolean received;
}
