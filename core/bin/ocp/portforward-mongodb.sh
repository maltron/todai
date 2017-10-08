oc port-forward $(oc get pods --output jsonpath='{.items[?(.metadata.labels.name=="mongodb")].metadata.name}') 27017:27017
