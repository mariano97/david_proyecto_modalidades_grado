<template>
  <div>
    <h2>
      <span id="gateway-page-heading">Gateway</span>
      <button class="btn btn-primary float-right" v-on:click="refresh()" :disabled="updatingRoutes">
        <font-awesome-icon icon="sync"></font-awesome-icon> <span>Refresh</span>
      </button>
    </h2>
    <h3>Current routes</h3>
    <div class="table-responsive">
      <table class="table table-striped">
        <thead>
          <tr>
            <th>URL</th>
            <th>Service</th>
            <th>Available servers</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="route of gatewayRoutes" :key="route.serviceId">
            <td>{{ route.path }}</td>
            <td>{{ route.serviceId }}</td>
            <td>
              <div v-if="route.serviceInstances.length === 0" class="label label-danger">Warning: no server available!</div>
              <div class="table-responsive">
                <table class="table table-striped" v-if="route">
                  <tr v-for="instance of route.serviceInstances" :key="instance.uri">
                    <td>
                      <a :href="instance.uri" target="_blank">{{ instance.uri }}</a>
                    </td>
                    <td>
                      <div
                        v-if="instance.instanceInfo"
                        class="badge"
                        :class="'badge-' + (instance.instanceInfo.status === 'UP' ? 'success' : 'danger')"
                      >
                        {{ instance.instanceInfo.status }}
                      </div>
                      <div v-if="!instance.instanceInfo" class="badge badge-warning">?</div>
                    </td>
                    <td>
                      <span v-for="(entry, key) of instance.metadata" :key="key">
                        <span class="badge badge-default font-weight-normal">
                          <span class="badge badge-pill badge-info font-weight-normal">{{ key }}</span>
                          {{ entry }}
                        </span>
                      </span>
                    </td>
                  </tr>
                </table>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script lang="ts" src="./gateway.component.ts"></script>
