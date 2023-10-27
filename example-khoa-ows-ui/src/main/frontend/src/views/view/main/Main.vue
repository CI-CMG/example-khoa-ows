<template>
  <div>
    <b-navbar fixed="top" class="csb-admin-nav" toggleable="sm" type="dark" variant="info">
      <b-navbar-toggle :target="navbarId" class="mr-2"></b-navbar-toggle>
      <b-navbar-nav class="ml-auto">
      </b-navbar-nav>
    </b-navbar>
    <div style="margin-top: 50px;" class="ml-2 mr-2">
      <router-view/>
    </div>
  </div>
</template>

<script>
import genId from '@/idGenerator';

export default {
  data() {
    return {
      navbarId: '',
      routeOptions: [
        // { route: 'Provider', value: 'Providers', roles: ['ROLE_CROWBAR_PROVIDER_READ'] },
        // { route: 'User', value: 'Users', roles: ['ROLE_CROWBAR_USER_READ'] },
        // { route: 'Audit', value: 'Audit', roles: ['ROLE_CROWBAR_AUDIT_READ'] },
        // { route: 'Filter', value: 'Filter', roles: ['ROLE_CROWBAR_GEOMETRY_FILTER_READ'] },
        // { route: 'Index', value: 'CSB Data', roles: ['ROLE_CROWBAR_INDEX_READ'] },
        // { route: 'QueueMonitor', value: 'Queue Monitor', roles: ['ROLE_CROWBAR_ADMIN_AUTHORITY'] },
        // { route: 'Approval', value: 'Approve', roles: ['ROLE_CROWBAR_USER_INDEX_READ'] },
      ],
    };
  },
  beforeMount() {
    this.navbarId = genId();
  },
  computed: {

  },
  methods: {

  },
};
</script>
