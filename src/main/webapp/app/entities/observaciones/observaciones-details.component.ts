import { Component, Vue, Inject } from 'vue-property-decorator';

import { IObservaciones } from '@/shared/model/observaciones.model';
import ObservacionesService from './observaciones.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class ObservacionesDetails extends Vue {
  @Inject('observacionesService') private observacionesService: () => ObservacionesService;
  @Inject('alertService') private alertService: () => AlertService;

  public observaciones: IObservaciones = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.observacionesId) {
        vm.retrieveObservaciones(to.params.observacionesId);
      }
    });
  }

  public retrieveObservaciones(observacionesId) {
    this.observacionesService()
      .find(observacionesId)
      .then(res => {
        this.observaciones = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
