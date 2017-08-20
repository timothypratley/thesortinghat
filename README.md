# The Sorting Hat

Reads records containing facts about people.
Displays lists of people sorted by their attributes.

>Enchanted hat that once belonged to Godric Gryffindor and sorts students into Hogwarts houses

## Usage

The Sorting Hat is written in [Clojure](https://www.clojure.org/) and requires [Leiningen](https://leiningen.org/) to be installed.

### Command Line interface

To read a file using the command line interface:

`lein run <input-file>`


### REST API

Start the server:

`lein ring server`

To submit records via curl:

```
curl -X POST \
     -H 'Content-Type: application/text' \
     -d 'Potter, Harry, male, green, 1/1/1985' \
     localhost:3000/records
```

To retrieve records:

1. `curl http://localhost:3000/records/gender`
2. `curl http://localhost:3000/records/birthdate`
3. `curl http://localhost:3000/records/name`

## Developing

To run the tests:

`lein test` or `lein test-refresh`

To check code coverage:

`lein cloverage`


## License

Copyright © 2017 Timothy Pratley

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
