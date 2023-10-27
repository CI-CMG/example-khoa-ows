<template>
  <div v-if="loadingFeatures">
    <b-spinner/>
  </div>
  <div v-else>
    <b-table striped hover :items="items" :fields="fields"></b-table>
  </div>
</template>

<script>
import { mapActions, mapState } from 'vuex';

export default {
  data() {
    return {
      fields: [
        { key: 'id', sortable: true },
        { key: 'gebcoFeatureStateId', sortable: true },
        { key: 'owsNote', sortable: true },
      ],
    };
  },

  computed: {
    ...mapState('ows', ['features', 'loadingFeatures']),
  },

  methods: {
    ...mapActions('ows', ['loadFeatures']),
  },

  mounted() {
    this.loadFeatures();
  },
};
</script>
