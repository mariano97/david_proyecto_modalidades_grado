import { Component, Vue, Inject } from 'vue-property-decorator';

import { IProfesor } from '@/shared/model/profesor.model';
import ProfesorService from './profesor.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class ProfesorDetails extends Vue {
  @Inject('profesorService') private profesorService: () => ProfesorService;
  @Inject('alertService') private alertService: () => AlertService;

  public profesor: IProfesor = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.profesorId) {
        vm.retrieveProfesor(to.params.profesorId);
      }
    });
  }

  public retrieveProfesor(profesorId) {
    this.profesorService()
      .find(profesorId)
      .then(res => {
        this.profesor = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
