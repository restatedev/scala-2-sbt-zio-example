## Scala ZIO example

To run:

```shell
sbt run
```

To register the service:

```shell
restate dp register http://localhost:9080 
```

To add a person to the address book:

```shell
curl -v http://localhost:8080/AddressBook/my-address-book/add --json '{"name":"Francesco"}'
```

To get the address book:

```shell
curl -v http://localhost:8080/AddressBook/my-address-book/get
```