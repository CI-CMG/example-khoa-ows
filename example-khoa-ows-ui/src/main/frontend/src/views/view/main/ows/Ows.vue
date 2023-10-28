<template>
  <div>
    <div v-if="loadingFeatures">
      <b-spinner/>
    </div>
    <div v-else>
      <div>
        <b-button variant="primary" class="m-1" @click="loadFeatures"><b-icon icon="arrow-clockwise"/></b-button>
        <b-button variant="primary" class="m-1" @click="newFeature"><b-icon icon="plus-square"/></b-button>
      </div>
      <b-table striped hover :items="features" :fields="fields"></b-table>
    </div>
    <b-modal v-model="modalOpen" :title="modalTitle" :hide-footer="true">
      <b-form class="m-2" @submit.prevent="saveForm" @reset.prevent="reset">

        <b-form-group label="Name" :label-for="nameId">
          <b-form-input
            :id="nameId"
            type="text"
            @blur="() => setTouched({path: 'name', touched: true})"
            :value="getValue('name')"
            @update="(value) => setValue({ path: 'name', value })"
            :state="showError('name')"
          />
          <b-form-invalid-feedback>{{ getError('name') }}</b-form-invalid-feedback>
        </b-form-group>

        <b-form-group label="Generic Term" :label-for="genericTermId">
          <b-form-input
            :id="genericTermId"
            type="text"
            @blur="() => setTouched({path: 'genericTerm', touched: true})"
            :value="getValue('genericTerm')"
            @update="(value) => setValue({ path: 'genericTerm', value })"
            :state="showError('genericTerm')"
          />
          <b-form-invalid-feedback>{{ getError('genericTerm') }}</b-form-invalid-feedback>
        </b-form-group>

        <b-form-group label="Feature State" :label-for="gebcoApprovalStateId">
          <b-form-input
            :id="gebcoApprovalStateId"
            :value="getValue('gebcoApprovalState')"
            :readonly="true"
          ></b-form-input>
        </b-form-group>

        <SearchCardCol
          :id="geometryId"
          title="Geometries"
          field="geometries"
          module="featureForm"/>

        <b-form-group label="Discoverer ID" :label-for="discovererIdId">
          <b-form-input
            :id="discovererIdId"
            type="text"
            @blur="() => setTouched({path: 'discovererId', touched: true})"
            :value="getValue('discovererId')"
            @update="(value) => setValue({ path: 'discovererId', value })"
            :state="showError('discovererId')"
          />
          <b-form-invalid-feedback>{{ getError('discovererId') }}</b-form-invalid-feedback>
        </b-form-group>

        <b-form-group label="Discovery Year" :label-for="discoveryYearId">
          <b-form-input
            :id="discoveryYearId"
            type="text"
            @blur="() => setTouched({path: 'discoveryYear', touched: true})"
            :value="getValue('discoveryYear')"
            @update="(value) => setValue({ path: 'discoveryYear', value })"
            :state="showError('discoveryYear')"
          />
          <b-form-invalid-feedback>{{ getError('discoveryYear') }}</b-form-invalid-feedback>
        </b-form-group>

        <b-form-group label="Proposer ID" :label-for="proposerIdId">
          <b-form-input
            :id="proposerIdId"
            type="text"
            @blur="() => setTouched({path: 'proposerId', touched: true})"
            :value="getValue('proposerId')"
            @update="(value) => setValue({ path: 'proposerId', value })"
            :state="showError('proposerId')"
          />
          <b-form-invalid-feedback>{{ getError('proposerId') }}</b-form-invalid-feedback>
        </b-form-group>

        <b-form-group label="Proposal Year" :label-for="proposalYearId">
          <b-form-input
            :id="proposalYearId"
            type="text"
            @blur="() => setTouched({path: 'proposalYear', touched: true})"
            :value="getValue('proposalYear')"
            @update="(value) => setValue({ path: 'proposalYear', value })"
            :state="showError('proposalYear')"
          />
          <b-form-invalid-feedback>{{ getError('proposalYear') }}</b-form-invalid-feedback>
        </b-form-group>

        <SearchCardCol
          :id="meetingId"
          title="Meetings"
          field="meetings"
          module="featureForm"/>

        <b-form-group label="Min Depth" :label-for="minDepthId">
          <b-form-input
            :id="minDepthId"
            type="text"
            @blur="() => setTouched({path: 'minDepth', touched: true})"
            :value="getValue('minDepth')"
            @update="(value) => setValue({ path: 'minDepth', value })"
            :state="showError('minDepth')"
          />
          <b-form-invalid-feedback>{{ getError('minDepth') }}</b-form-invalid-feedback>
        </b-form-group>

        <b-form-group label="Max Depth" :label-for="maxDepthId">
          <b-form-input
            :id="maxDepthId"
            type="text"
            @blur="() => setTouched({path: 'maxDepth', touched: true})"
            :value="getValue('maxDepth')"
            @update="(value) => setValue({ path: 'maxDepth', value })"
            :state="showError('maxDepth')"
          />
          <b-form-invalid-feedback>{{ getError('maxDepth') }}</b-form-invalid-feedback>
        </b-form-group>

        <b-form-group label="Total Relief" :label-for="totalReliefId">
          <b-form-input
            :id="totalReliefId"
            type="text"
            @blur="() => setTouched({path: 'totalRelief', touched: true})"
            :value="getValue('totalRelief')"
            @update="(value) => setValue({ path: 'totalRelief', value })"
            :state="showError('totalRelief')"
          />
          <b-form-invalid-feedback>{{ getError('totalRelief') }}</b-form-invalid-feedback>
        </b-form-group>

        <b-form-group label="Dimension" :label-for="dimensionId">
          <b-form-input
            :id="dimensionId"
            type="text"
            @blur="() => setTouched({path: 'dimension', touched: true})"
            :value="getValue('dimension')"
            @update="(value) => setValue({ path: 'dimension', value })"
            :state="showError('dimension')"
          />
          <b-form-invalid-feedback>{{ getError('dimension') }}</b-form-invalid-feedback>
        </b-form-group>

        <b-form-group label="History" :label-for="historyId">
          <b-form-input
            :id="historyId"
            type="text"
            @blur="() => setTouched({path: 'history', touched: true})"
            :value="getValue('history')"
            @update="(value) => setValue({ path: 'history', value })"
            :state="showError('history')"
          />
          <b-form-invalid-feedback>{{ getError('history') }}</b-form-invalid-feedback>
        </b-form-group>

        <b-form-group label="Remarks" :label-for="remarksId">
          <b-form-input
            :id="remarksId"
            type="text"
            @blur="() => setTouched({path: 'remarks', touched: true})"
            :value="getValue('remarks')"
            @update="(value) => setValue({ path: 'remarks', value })"
            :state="showError('remarks')"
          />
          <b-form-invalid-feedback>{{ getError('remarks') }}</b-form-invalid-feedback>
        </b-form-group>

        <b-form-group label="Comments" :label-for="commentsId">
          <b-form-input
            :id="commentsId"
            type="text"
            @blur="() => setTouched({path: 'comments', touched: true})"
            :value="getValue('comments')"
            @update="(value) => setValue({ path: 'comments', value })"
            :state="showError('comments')"
          />
          <b-form-invalid-feedback>{{ getError('comments') }}</b-form-invalid-feedback>
        </b-form-group>

        <b-form-group label="OWS Note" :label-for="owsNoteId">
          <b-form-input
            :id="owsNoteId"
            type="text"
            @blur="() => setTouched({path: 'owsNote', touched: true})"
            :value="getValue('owsNote')"
            @update="(value) => setValue({ path: 'owsNote', value })"
            :state="showError('owsNote')"
          />
          <b-form-invalid-feedback>{{ getError('owsNote') }}</b-form-invalid-feedback>
        </b-form-group>

        <b-form-group label="ID" :label-for="idId">
          <b-form-input
            :id="idId"
            :value="getValue('id')"
            :readonly="true"
          ></b-form-input>
        </b-form-group>

        <b-form-group label="GEBCO Feature Version" :label-for="gebcoFeatureVersionId">
          <b-form-input
            :id="gebcoFeatureVersionId"
            :value="getValue('gebcoFeatureVersion')"
            :readonly="true"
          ></b-form-input>
        </b-form-group>

        <b-form-group label="GEBCO Feature ID" :label-for="gebcoFeatureStateIdId">
          <b-form-input
            :id="gebcoFeatureStateIdId"
            :value="getValue('gebcoFeatureStateId')"
            :readonly="true"
          ></b-form-input>
        </b-form-group>

        <div>
          <b-button v-if="showSubmit" type="submit" variant="primary" class="mb-2 mr-sm-2 mb-sm-0 mr-3">Save</b-button>
          <b-button v-if="formDirty" type="reset" variant="danger" class="mb-2 mr-sm-2 mb-sm-0">Reset</b-button>
        </div>
      </b-form>

    </b-modal>
  </div>
</template>

<script>
import {
  mapActions, mapGetters, mapMutations, mapState,
} from 'vuex';
import genId from '@/idGenerator';
import SearchCardCol from '@/components/SearchCardCol.vue';

export default {
  components: {
    SearchCardCol,
  },
  data() {
    return {
      idId: '',
      nameId: '',
      genericTermId: '',
      geometryId: '',
      proposerIdId: '',
      discovererIdId: '',
      discoveryYearId: '',
      proposalYearId: '',
      meetingId: '',
      minDepthId: '',
      maxDepthId: '',
      totalReliefId: '',
      dimensionId: '',
      historyId: '',
      remarksId: '',
      commentsId: '',
      gebcoFeatureStateIdId: '',
      gebcoFeatureVersionId: '',
      gebcoApprovalStateId: '',
      owsNoteId: '',

      modalOpen: false,
      modalMode: 'New',
      fields: [
        { key: 'name', sortable: true },
        { key: 'genericTerm', sortable: true },
        { key: 'gebcoApprovalState', sortable: true },
        { key: 'id', sortable: true },
        { key: 'gebcoFeatureStateId', sortable: true },
        { key: 'owsNote', sortable: true },
      ],
    };
  },

  computed: {
    ...mapState('ows', ['features', 'loadingFeatures']),
    ...mapGetters('featureForm',
      [
        'getValue',
        'formDirty',
        'getError',
        'isTouched',
        'formHasUntouchedErrors',
      ]),
    modalTitle() {
      return `${this.modalMode} Feature`;
    },
    showError() {
      return (path) => ((!this.isTouched(path) && this.getError(path)) ? false : null);
    },
    showSubmit() {
      return this.formDirty && !this.formHasUntouchedErrors;
    },
  },

  methods: {
    ...mapMutations('featureForm',
      [
        'initialize',
        'setValue',
        'setTouched',
        'setError',
        'deleteFromArray',
        'addToArray',
      ]),
    ...mapActions('ows', ['loadFeatures']),
    ...mapActions('featureForm', ['submit', 'reset']),
    newFeature() {
      if (!this.modalOpen) {
        this.initialize();
        this.modalMode = 'New';
        this.modalOpen = true;
      }
    },
    saveForm() {
      this.submit()
        // .then((user) => this.save({ user, id: this.id }))
        .then((feature) => {
          console.log(feature);
          this.modalOpen = false;
        });
    },
  },

  mounted() {
    this.idId = genId();
    this.nameId = genId();
    this.genericTermId = genId();
    this.geometryId = genId();
    this.proposerIdId = genId();
    this.discovererIdId = genId();
    this.discoveryYearId = genId();
    this.proposalYearId = genId();
    this.meetingId = genId();
    this.minDepthId = genId();
    this.maxDepthId = genId();
    this.totalReliefId = genId();
    this.dimensionId = genId();
    this.historyId = genId();
    this.remarksId = genId();
    this.commentsId = genId();
    this.gebcoFeatureStateIdId = genId();
    this.gebcoFeatureVersionId = genId();
    this.gebcoApprovalStateId = genId();
    this.owsNoteId = genId();
    this.loadFeatures();
  },
};
</script>
