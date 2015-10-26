# 2.1 Unwdmi Server
Something something but BETTER!

Javazaken en dergelijk voor Hanze Hogeschool Informatica, Thema 2.1.

Projectje over verwerking van weergegevens.

:rainbow: :sunny: :cloud: :umbrella: :snowflake: :rainbow:

## Building

```
make build
```

## Running

The app is basically nothing more than a chain of processors with different
sources and endpoints.

Listen for clients pushing XML weather data and store in postgres DB or a file:

```bash
./run --save postgres # default table is "weather_measurements"
./run --save postgres --table my_own_table # table is created if necessary
./run --save tsv --file measurements.tsv # file is created if necessary
```

> Optionally, you can also add `--batch 5000` to the above commands to change
  the batch size, or `--batch 0` to disable batching altogether. Default batch
  size is 2000 measurements.

> Optionally, you can also add `--monitor 2` to change the Updates Monitor
  interval, or `--monitor 0` to disable processing speed monitoring altogether.
  The default interval is 5 seconds.

Query data from a TSV file, outputting results as TSV, too:

```bash
./run --load measurements.tsv --query avg:temperatureByCountry
./run --load measurements.tsv --query min:temperature
```

Or if you want more flexibility:

```bash
./run --load measurements.tsv \
      --query {avg,min,max} \
      --value {temperature,dewpoint,stationpressure,sealevelpressure,visibility,windspeed,precipitation,snowdepth,cloudcover,winddirection} \ # defaults to "temperature"
      --group-by {all,country,station} # defaults to "all"
```

Copying data from a TSV file to a database:

```
./run --load measurements.tsv --save postgres --table tsv_backup
```

## Nuttige Git-shit

Haal repo binnen met de SSH Clone URL, als het goed is staan je remotes meteen
goed.

```bash
$ git clone git@github.com:IcyPalm/2.1EINDBazen.git
```

Staan je remotes niet goed, dan doe je ook nog:

```bash
$ git remote add origin git@github.com:IcyPalm/2.1EINDBazen.git
```

Maak voordat je ergens aan werkt, éérst een nieuwe branch aan! Dan hoeven we
geen rekening te houden met wat voor aanpassingen anderen doen. Via command line
gaat dat zo:

```bash
# eerst de meest recente master van github halen:
$ git checkout master
$ git pull
# nu een nieuwe branch aanmaken, gebaseerd op master:
$ git branch mijn-epische-feature
$ git checkout mijn-epische-feature
Switched to branch 'mijn-epische-feature'
```

Als je dan dingen aanpast, kun je pushen naar Github met:

```bash
$ git push origin mijn-epische-feature
```

En dan via github mergen als je denkt dat het ook daadwerkelijk een epische
feature is, anders kun je eerst even een Pull Request openen zodat anderen het
kunnen nakijken.


**Werk dus nooit direct op master!**

## License

[MIT](./LICENSE)
