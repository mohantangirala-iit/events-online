import { Role } from 'app/entities/enumerations/role.model';
import { Level } from 'app/entities/enumerations/level.model';

export interface IPersonType {
  id: number;
  jobTitle?: number | null;
  role?: keyof typeof Role | null;
  level?: keyof typeof Level | null;
}

export type NewPersonType = Omit<IPersonType, 'id'> & { id: null };
