
docker run \
  --name testesio  \
  -e MONGO_INITDB_ROOT_USERNAME=admin \
  -e MONGO_INITDB_ROOT_PASSWORD=admin \
  -e MONGO_INITDB_DATABASE=testesio \
  --rm \
  -p 27017:27017 \
  -v /opt/db/testesio:/var/lib/mongo/data \
  -d mongo