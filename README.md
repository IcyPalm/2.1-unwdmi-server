# 2.1EINDbazen
Something something but BETTER!

Javazaken en dergelijk voor Hanze Hogeschool Informatica, Thema 2.1.

Projectje over verwerking van weergegevens.

:rainbow: :sunny: :cloud: :umbrella: :snowflake: :rainbow:

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
