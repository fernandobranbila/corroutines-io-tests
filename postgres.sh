
docker run --name r2dbc-coroutines --rm -e POSTGRES_PASSWORD=123456 -e POSTGRES_DB=r2dbc-coroutines -p 5433:5432 -v /opt/db/r2dbc-coroutines:/var/lib/postgresql/data -d postgres