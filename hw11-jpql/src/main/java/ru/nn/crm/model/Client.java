package ru.nn.crm.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//@Getter
//@Setter
@NoArgsConstructor
@Entity
@Table(name = "clients")
public class Client implements Cloneable {

    @Getter
    @Setter
    @Id
    @SequenceGenerator(name = "client_gen", sequenceName = "client_seq", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "client_gen")
    @Column(name = "id")
    private Long id;

    @Getter
    @Setter
    @Column(name = "name")
    private String name;

    @OneToOne(mappedBy = "client", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private Address address;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Phone> phones;

    public Client(String name) {
        this.id = null;
        this.name = name;
        phones = new ArrayList<>();
    }

    public Client(Long id, String name) {
        this.id = id;
        this.name = name;
        phones = new ArrayList<>();
    }

    public Client(Long id, String name, Address address, List<Phone> phones) {
        this.id = id;
        this.name = name;
        this.address = address;
        if (this.address != null) {
            this.address.setClient(this);
        }
        this.phones = phones;
        for (var phone : phones) {
            phone.setClient(this);
        }
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
        if (this.address != null) {
            this.address.setClient(this);
            }
    }

    public List<Phone> getPhones() {
        return phones;
    }

    public void setPhones(List<Phone> phones) {
        this.phones = phones;
        for (var phone : this.phones) {
            phone.setClient(this);
        }
    }

    @Override
    @SuppressWarnings({"java:S2975", "java:S1182"})
    public Client clone() {
        Client client = new Client();

        var clonedPhones = new ArrayList<Phone>();
        for (var phone : this.phones) {
            var clonedPhone = phone.clone();
            clonedPhone.setClient(client);
            clonedPhones.add(clonedPhone);
        }
        Address clonedAddress = null;
        if (this.address != null) {
            clonedAddress = this.address.clone();
            clonedAddress.setClient(client);
        }

        client.setId(this.id);
        client.setName(this.name);
        client.setAddress(clonedAddress);
        client.setPhones(clonedPhones);
        return client;
    }

    @Override
    public String toString() {
        return "Client{id=" + id + ", name='" + name + "', address='" + address + ", phones=" + phones + "}";
    }
}
