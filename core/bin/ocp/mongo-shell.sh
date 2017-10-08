oc exec -ti $(oc get pods --output jsonpath='{.items[?(.metadata.labels.name=="mongodb")].metadata.name}') -- bash -c 'mongo 127.0.0.1:27017/$MONGODB_DATABASE -u $MONGODB_USER -p $MONGODB_PASSWORD'
