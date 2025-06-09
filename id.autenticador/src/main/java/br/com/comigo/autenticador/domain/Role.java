package br.com.comigo.autenticador.domain;

public class Role {
    public enum Status {
        ACTIVE,
        INACTIVE,
    }

    private String name;
    private Status status;

    public Role() {}

    public Role(String name, Status status) {
        this.name = name;
        this.status = status;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Status getStatus() {
        return this.status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}