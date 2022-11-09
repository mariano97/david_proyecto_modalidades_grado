/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import ObservacionesComponent from '@/entities/observaciones/observaciones.vue';
import ObservacionesClass from '@/entities/observaciones/observaciones.component';
import ObservacionesService from '@/entities/observaciones/observaciones.service';
import AlertService from '@/shared/alert/alert.service';

const localVue = createLocalVue();
localVue.use(ToastPlugin);

config.initVueApp(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('b-badge', {});
localVue.component('jhi-sort-indicator', {});
localVue.directive('b-modal', {});
localVue.component('b-button', {});
localVue.component('router-link', {});

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  describe('Observaciones Management Component', () => {
    let wrapper: Wrapper<ObservacionesClass>;
    let comp: ObservacionesClass;
    let observacionesServiceStub: SinonStubbedInstance<ObservacionesService>;

    beforeEach(() => {
      observacionesServiceStub = sinon.createStubInstance<ObservacionesService>(ObservacionesService);
      observacionesServiceStub.retrieve.resolves({ headers: {} });

      wrapper = shallowMount<ObservacionesClass>(ObservacionesComponent, {
        store,
        localVue,
        stubs: { jhiItemCount: true, bPagination: true, bModal: bModalStub as any },
        provide: {
          observacionesService: () => observacionesServiceStub,
          alertService: () => new AlertService(),
        },
      });
      comp = wrapper.vm;
    });

    it('Should call load all on init', async () => {
      // GIVEN
      observacionesServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

      // WHEN
      comp.retrieveAllObservacioness();
      await comp.$nextTick();

      // THEN
      expect(observacionesServiceStub.retrieve.called).toBeTruthy();
      expect(comp.observaciones[0]).toEqual(expect.objectContaining({ id: 123 }));
    });

    it('should load a page', async () => {
      // GIVEN
      observacionesServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });
      comp.previousPage = 1;

      // WHEN
      comp.loadPage(2);
      await comp.$nextTick();

      // THEN
      expect(observacionesServiceStub.retrieve.called).toBeTruthy();
      expect(comp.observaciones[0]).toEqual(expect.objectContaining({ id: 123 }));
    });

    it('should re-initialize the page', async () => {
      // GIVEN
      observacionesServiceStub.retrieve.reset();
      observacionesServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

      // WHEN
      comp.loadPage(2);
      await comp.$nextTick();
      comp.clear();
      await comp.$nextTick();

      // THEN
      expect(observacionesServiceStub.retrieve.callCount).toEqual(2);
      expect(comp.page).toEqual(1);
      expect(comp.observaciones[0]).toEqual(expect.objectContaining({ id: 123 }));
    });

    it('should calculate the sort attribute for an id', () => {
      // WHEN
      const result = comp.sort();

      // THEN
      expect(result).toEqual(['id,asc']);
    });

    it('should calculate the sort attribute for a non-id attribute', () => {
      // GIVEN
      comp.propOrder = 'name';

      // WHEN
      const result = comp.sort();

      // THEN
      expect(result).toEqual(['name,asc', 'id']);
    });
    it('Should call delete service on confirmDelete', async () => {
      // GIVEN
      observacionesServiceStub.delete.resolves({});

      // WHEN
      comp.prepareRemove({ id: 123 });
      expect(observacionesServiceStub.retrieve.callCount).toEqual(1);

      comp.removeObservaciones();
      await comp.$nextTick();

      // THEN
      expect(observacionesServiceStub.delete.called).toBeTruthy();
      expect(observacionesServiceStub.retrieve.callCount).toEqual(2);
    });
  });
});
