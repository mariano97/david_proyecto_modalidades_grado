import { Component, Vue, Inject } from 'vue-property-decorator';

import { IArl } from '@/shared/model/arl.model';
import ArlService from './arl.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class ArlDetails extends Vue {
  @Inject('arlService') private arlService: () => ArlService;
  @Inject('alertService') private alertService: () => AlertService;

  public arl: IArl = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.arlId) {
        vm.retrieveArl(to.params.arlId);
      }
    });
  }

  public retrieveArl(arlId) {
    this.arlService()
      .find(arlId)
      .then(res => {
        this.arl = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
