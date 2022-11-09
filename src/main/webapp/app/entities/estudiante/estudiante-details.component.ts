import { Component, Vue, Inject } from 'vue-property-decorator';

import { IEstudiante } from '@/shared/model/estudiante.model';
import EstudianteService from './estudiante.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class EstudianteDetails extends Vue {
  @Inject('estudianteService') private estudianteService: () => EstudianteService;
  @Inject('alertService') private alertService: () => AlertService;

  public estudiante: IEstudiante = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.estudianteId) {
        vm.retrieveEstudiante(to.params.estudianteId);
      }
    });
  }

  public retrieveEstudiante(estudianteId) {
    this.estudianteService()
      .find(estudianteId)
      .then(res => {
        this.estudiante = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
