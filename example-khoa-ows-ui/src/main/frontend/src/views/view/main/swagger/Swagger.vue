<template>
  <div ref="swagger"/>
</template>

<script>
import { SwaggerUIBundle, SwaggerUIStandalonePreset } from 'swagger-ui-dist';
import { mapGetters } from 'vuex';
import { BASE_PATH } from '@/basePath';

export default {
  computed: {
    ...mapGetters('user', ['token']),
  },
  mounted() {
    SwaggerUIBundle({
      domNode: this.$refs.swagger,
      urls: [
        { url: `${BASE_PATH}/docs/api/provider`, name: 'Provider API' },
        { url: `${BASE_PATH}/docs/api/v1-admin`, name: 'V1 Admin API' },
      ],
      deepLinking: true,
      presets: [
        SwaggerUIBundle.presets.apis,
        SwaggerUIStandalonePreset,
      ],
      plugins: [
        SwaggerUIBundle.plugins.DownloadUrl,
      ],
      layout: 'StandaloneLayout',
      tagsSorter: 'alpha',
    });
  },
};
</script>
