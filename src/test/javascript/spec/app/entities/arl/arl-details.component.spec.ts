/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import ArlDetailComponent from '@/entities/arl/arl-details.vue';
import ArlClass from '@/entities/arl/arl-details.component';
import ArlService from '@/entities/arl/arl.service';
import router from '@/router';
import AlertService from '@/shared/alert/alert.service';

const localVue = createLocalVue();
localVue.use(VueRouter);

config.initVueApp(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('router-link', {});

describe('Component Tests', () => {
  describe('Arl Management Detail Component', () => {
    let wrapper: Wrapper<ArlClass>;
    let comp: ArlClass;
    let arlServiceStub: SinonStubbedInstance<ArlService>;

    beforeEach(() => {
      arlServiceStub = sinon.createStubInstance<ArlService>(ArlService);

      wrapper = shallowMount<ArlClass>(ArlDetailComponent, {
        store,
        localVue,
        router,
        provide: { arlService: () => arlServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundArl = { id: 123 };
        arlServiceStub.find.resolves(foundArl);

        // WHEN
        comp.retrieveArl(123);
        await comp.$nextTick();

        // THEN
        expect(comp.arl).toBe(foundArl);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundArl = { id: 123 };
        arlServiceStub.find.resolves(foundArl);

        // WHEN
        comp.beforeRouteEnter({ params: { arlId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.arl).toBe(foundArl);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        comp.previousState();
        await comp.$nextTick();

        expect(comp.$router.currentRoute.fullPath).toContain('/');
      });
    });
  });
});
