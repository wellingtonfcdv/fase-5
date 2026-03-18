package com.fiap.task_service;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "tasks")
public class Task {

    enum Status {
        OPEN, IN_PROGRESS, COMPLETED, CANCELED
    }

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "task_seq")
    @SequenceGenerator(name = "task_seq", sequenceName = "task_seq", allocationSize = 1)
    private Long id;

    private String title;

    private String description;

    @Enumerated(EnumType.STRING)
    private Status status;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false, updatable = true)
    private Instant updatedAt;

    @Column(name = "deleted_at", nullable = true, updatable = true)
    private Instant deletedAt;

    private Task(final String title, final String description, final Status status) {
        this.title = title;
        this.description = description;
        this.status = status;
    }

    public static Task newTask(final String title, final String description) {
        return new Task(title, description, Status.OPEN);
    }

    public void cancel() {
        if (this.getStatus().equals(Status.CANCELED)) {
            throw new TaskAlreadyCanceledException(this.getId());
        }

        if (this.getStatus().equals(Status.COMPLETED)) {
            throw new TaskAlreadytCompletedException(this.getId());
        }

        this.setDeletedAt(Instant.now());
        this.setStatus(Status.CANCELED);
    }

    public void execute() {
        if (!this.getStatus().equals(Status.OPEN)) {
            throw new TaskAlreadyInProgressException(this.getId());
        }

        this.setStatus(Status.IN_PROGRESS);
    }

    public void complete() {
        if (!this.getStatus().equals(Status.IN_PROGRESS)) {
            throw new TaskAlreadytCompletedException(this.getId());
        }

        this.setStatus(Status.COMPLETED);
    }

    public void update(final String title, final String description) {
        this.setTitle(title);
        this.setDescription(description);
    }
}
