# The Sorting Hat

Reads records containing facts about people.
Displays lists of people sorted by their attributes.

>Enchanted hat that once belonged to Godric Gryffindor and sorts students into Hogwarts houses

![Image of The Sorting Hat](https://images.pottermore.com/bxd3o8b291gf/2sLwPSOVqoOyCkEgSk0Oek/2c207d3a33dc10649554efc3014152da/MinervaMcGonagall_PM_B1C7M2_HarryPotterBeingSortedInGreatHall_Moment.jpg?w=1100&q=85)

## Usage

The Sorting Hat is written in [Clojure](https://www.clojure.org/) and requires [Leiningen](https://leiningen.org/) to be installed.

### Command Line interface

To read a file using the command line interface:

`lein run <input-file>`

Example input files are provided in resources:

`lein run resources/people.csv`

Input files are expected to consist of only records separated by lines
with field separated by commas, pipes, or spaces.
Fields may be quoted if they contain a separator.


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

```
curl -X POST \
     -H 'Content-Type: application/text' \
     -d 'Granger | Hermione | female | red | 3/1/1985' \
     localhost:3000/records
```

```
curl -X POST \
     -H 'Content-Type: application/text' \
     -d 'Weasley Ginny female orange 4/4/1986' \
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
