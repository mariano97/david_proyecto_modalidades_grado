import { Component, Vue, Inject } from 'vue-property-decorator';

import { ITablaMaestra } from '@/shared/model/tabla-maestra.model';
import TablaMaestraService from './tabla-maestra.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class TablaMaestraDetails extends Vue {
  @Inject('tablaMaestraService') private tablaMaestraService: () => TablaMaestraService;
  @Inject('alertService') private alertService: () => AlertService;

  public tablaMaestra: ITablaMaestra = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.tablaMaestraId) {
        vm.retrieveTablaMaestra(to.params.tablaMaestraId);
      }
    });
  }

  public retrieveTablaMaestra(tablaMaestraId) {
    this.tablaMaestraService()
      .find(tablaMaestraId)
      .then(res => {
        this.tablaMaestra = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
